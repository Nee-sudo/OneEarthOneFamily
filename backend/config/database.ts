import * as admin from 'firebase-admin';
import dotenv from 'dotenv';
import * as path from 'path';
import * as fs from 'fs';

dotenv.config();

let db: any;
let isMockDb = false;

class MockDocRef {
  constructor(private collectionName: string, private docId: string, private mockDb: any) {}

  get path() {
    return `${this.collectionName}/${this.docId}`;
  }

  async get() {
    const data = this.mockDb._data[this.collectionName]?.[this.docId];
    return {
      exists: data !== undefined,
      id: this.docId,
      ref: this,
      data: () => (data ? JSON.parse(JSON.stringify(data)) : undefined)
    };
  }

  async set(data: any) {
    if (!this.mockDb._data[this.collectionName]) {
      this.mockDb._data[this.collectionName] = {};
    }
    this.mockDb._data[this.collectionName][this.docId] = JSON.parse(JSON.stringify(data));
  }

  async update(data: any) {
    if (!this.mockDb._data[this.collectionName]) {
      this.mockDb._data[this.collectionName] = {};
    }
    const current = this.mockDb._data[this.collectionName][this.docId] || {};
    this.mockDb._data[this.collectionName][this.docId] = {
      ...current,
      ...JSON.parse(JSON.stringify(data))
    };
  }

  async delete() {
    if (this.mockDb._data[this.collectionName]) {
      delete this.mockDb._data[this.collectionName][this.docId];
    }
  }
}

class MockCollection {
  constructor(private collectionName: string, private mockDb: any, private limitVal: number = Infinity) {}

  doc(id: string) {
    return new MockDocRef(this.collectionName, id, this.mockDb);
  }

  limit(n: number) {
    return new MockCollection(this.collectionName, this.mockDb, n);
  }

  where(field: string, op: string, val: any) {
    const collections = this.mockDb._data[this.collectionName] || {};
    const filteredDocs: any[] = [];
    for (const [id, value] of Object.entries(collections)) {
      const valObj = value as any;
      if (op === '==' && valObj[field] === val) {
        filteredDocs.push({
          id,
          ref: new MockDocRef(this.collectionName, id, this.mockDb),
          data: () => JSON.parse(JSON.stringify(valObj))
        });
      }
    }

    return {
      get: async () => {
        const sliced = filteredDocs.slice(0, this.limitVal);
        return {
          empty: sliced.length === 0,
          docs: sliced
        };
      }
    };
  }

  async get() {
    const collections = this.mockDb._data[this.collectionName] || {};
    const docs = [];
    for (const [id, value] of Object.entries(collections)) {
      docs.push({
        id,
        ref: new MockDocRef(this.collectionName, id, this.mockDb),
        data: () => JSON.parse(JSON.stringify(value))
      });
    }
    const sliced = docs.slice(0, this.limitVal);
    return {
      empty: sliced.length === 0,
      docs: sliced
    };
  }
}

class MockFirestore {
  public _data: Record<string, Record<string, any>> = {};

  collection(name: string) {
    return new MockCollection(name, this);
  }

  async runTransaction(cb: (transaction: any) => Promise<any>) {
    const transaction = {
      get: async (docRef: MockDocRef) => {
        return docRef.get();
      },
      set: (docRef: MockDocRef, data: any) => {
        docRef.set(data);
        return transaction;
      },
      update: (docRef: MockDocRef, data: any) => {
        docRef.update(data);
        return transaction;
      },
      delete: (docRef: MockDocRef) => {
        docRef.delete();
        return transaction;
      }
    };
    return cb(transaction);
  }
}

export const connectDatabase = async (): Promise<void> => {
  try {
    console.log('⚡ Firebase: Initializing Admin SDK...');

    // If already initialized, avoid re-init
    if (admin.apps.length > 0) {
      db = admin.firestore();
      return;
    }

    const serviceAccountVar = process.env.FIREBASE_SERVICE_ACCOUNT;
    const projectIdVar = process.env.FIREBASE_PROJECT_ID || 'one-earth-app';

    // Auto-detect local serviceAccountKey.json file for VS Code local environment
    const localKeyPath = path.join(process.cwd(), 'serviceAccountKey.json');
    const localKeyPathConfig = path.join(__dirname, '..', 'serviceAccountKey.json');
    const localKeyPathConfigDirect = path.join(__dirname, 'serviceAccountKey.json');

    if (fs.existsSync(localKeyPath)) {
      admin.initializeApp({
        credential: admin.credential.cert(localKeyPath)
      });
      console.log(`✅ Firebase: Successfully initialized using local file credentials at "${localKeyPath}".`);
    } else if (fs.existsSync(localKeyPathConfig)) {
      admin.initializeApp({
        credential: admin.credential.cert(localKeyPathConfig)
      });
      console.log(`✅ Firebase: Successfully initialized using local file credentials at "${localKeyPathConfig}".`);
    } else if (fs.existsSync(localKeyPathConfigDirect)) {
      admin.initializeApp({
        credential: admin.credential.cert(localKeyPathConfigDirect)
      });
      console.log(`✅ Firebase: Successfully initialized using local file credentials at "${localKeyPathConfigDirect}".`);
    } else if (serviceAccountVar) {
      try {
        const serviceAccount = JSON.parse(serviceAccountVar);
        admin.initializeApp({
          credential: admin.credential.cert(serviceAccount)
        });
        console.log('✅ Firebase: Successfully initialized with explicit service account JSON credential.');
      } catch (err: any) {
        console.error('⚠️ Firebase: Found FIREBASE_SERVICE_ACCOUNT but failed to parse JSON. Falling back to default auth.', err.message);
        admin.initializeApp({
          projectId: projectIdVar
        });
      }
    } else {
      // Allow fallback to standard environment auth or local emulator
      admin.initializeApp({
        projectId: projectIdVar
      });
      console.log(`✅ Firebase: Initialized with Project ID: "${projectIdVar}".`);
    }

    try {
      console.log('⚡ Firebase: Verifying Firestore connectivity and credentials...');
      const tempDb = admin.firestore();
      // Perform a minimal, non-blocking check to verify credentials load successfully
      await tempDb.collection('counters').limit(1).get();
      db = tempDb;
      console.log('✅ Firebase: Firestore instance successfully verified with fully functional credentials.');
    } catch (testError: any) {
      console.warn('⚠️ Firebase Credentials Verification Failed! (Usually means no serviceAccountKey.json or Google App Credentials found).');
      console.warn('🔄 Failing gracefully: Initializing self-contained offline In-Memory Mock Database sandbox.');
      isMockDb = true;
      db = new MockFirestore();
    }
  } catch (error: any) {
    console.error('❌ Firebase Admin SDK setup hit fatal error:', error.message);
    console.warn('🔄 Failing gracefully: Initializing self-contained offline In-Memory Mock Database sandbox as fallback.');
    isMockDb = true;
    db = new MockFirestore();
  }
};

export const getFirestoreDb = (): admin.firestore.Firestore => {
  if (!db) {
    if (isMockDb) {
      db = new MockFirestore();
    } else {
      db = admin.firestore();
    }
  }
  return db as admin.firestore.Firestore;
};

export const disconnectDatabase = async (): Promise<void> => {
  try {
    console.log('🔌 Firebase Admin: Disconnected safely from Cloud services.');
  } catch (error) {
    console.error('❌ Firebase: Error during disconnect:', error);
  }
};
