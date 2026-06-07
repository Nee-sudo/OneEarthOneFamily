import { getFirestoreDb } from './config/database';
import { IUser } from './models/User';
import { IPost } from './models/Post';
import { IComment } from './models/Comment';

export const seedDatabaseIfEmpty = async () => {
  try {
    const db = getFirestoreDb();

    // 1. Seed Users
    const usersSnapshot = await db.collection('users').limit(1).get();
    if (usersSnapshot.empty) {
      console.log('🌱 Firestore: Seeding administrative citizens...');
      const defaultUsers: IUser[] = [
        {
          id: "arjun@oneearth.io",
          name: "Arjun Patel",
          username: "@arjun_vision",
          email: "arjun@oneearth.io",
          dob: "1994-04-12",
          territory: "India",
          flagEmoji: "🇮🇳",
          gender: "Male",
          currentRank: "Royal Candidate",
          knowledgeCredits: 340,
          contributionCredits: 180,
          reputationScore: 99,
          personalityTraits: "Visionary,Leader,Philosopher,Teacher,Humanitarian",
          bio: "Coordinating organic local waste recycling systems in Maharashtra. Let's build the empire from the soil.",
          isCandidate: true,
          campaignVision: "United Global Ecological Safeguards",
          campaignManifesto: "My campaign centers around tying modern technology directly with micro-farming community actions. Let's make every territory garden green.",
          votesCount: 12,
          hasVoted: false,
          onboardingCompleted: true,
          citizenOathAccepted: true,
          followers: 120,
          following: 95,
          profilePhoto: "",
          passphrase: "1234",
          createdAt: Date.now(),
          updatedAt: Date.now()
        },
        {
          id: "clara@oneearth.io",
          name: "Clara Dupont",
          username: "@clara_sage",
          email: "clara@oneearth.io",
          dob: "1990-11-20",
          territory: "France",
          flagEmoji: "🇫🇷",
          gender: "Female",
          currentRank: "Guardian",
          knowledgeCredits: 410,
          contributionCredits: 150,
          reputationScore: 98,
          personalityTraits: "Scientist,Educator,Philosopher,Visionary,Creator",
          bio: "Physics educator. Translating scientific literacy into open global civic solutions.",
          isCandidate: true,
          campaignVision: "Absolute Scientific Open Access",
          campaignManifesto: "Education is the foundation of meritocratic leadership. I intend to build the Open Digital Library with zero economic payload.",
          votesCount: 15,
          hasVoted: false,
          onboardingCompleted: true,
          citizenOathAccepted: true,
          followers: 140,
          following: 110,
          profilePhoto: "",
          passphrase: "1234",
          createdAt: Date.now(),
          updatedAt: Date.now()
        },
        {
          id: "kofi@oneearth.io",
          name: "Kofi Mensah",
          username: "@kofi_builder",
          email: "kofi@oneearth.io",
          dob: "1988-06-03",
          territory: "Kenya",
          flagEmoji: "🇰🇪",
          gender: "Male",
          currentRank: "Contributor",
          knowledgeCredits: 175,
          contributionCredits: 210,
          reputationScore: 97,
          personalityTraits: "Builder,Humanitarian,Leader,Explorer,Creator",
          bio: "Constructing modular solar installations in dry-zones across East Africa.",
          isCandidate: true,
          campaignVision: "Grassroots Infrastructure Mobilization",
          campaignManifesto: "Action outweighs debate. I will introduce regional solar and clean water blueprints as active Imperial missions for collective credit.",
          votesCount: 8,
          hasVoted: false,
          onboardingCompleted: true,
          citizenOathAccepted: true,
          followers: 85,
          following: 75,
          profilePhoto: "",
          passphrase: "1234",
          createdAt: Date.now(),
          updatedAt: Date.now()
        }
      ];

      for (const u of defaultUsers) {
        await db.collection('users').doc(u.id).set(u);
      }
      console.log('✅ Firestore: Administrative citizens successfully seeded.');
    }

    // Ensure our test user is present in Firestore
    const testUserRef = db.collection('users').doc('test@oneearth.io');
    const testUserDoc = await testUserRef.get();
    if (!testUserDoc.exists) {
      console.log('🌱 Firestore: Seeding test citizen...');
      const testUser: IUser = {
        id: "test@oneearth.io",
        name: "Test Citizen",
        username: "@test_citizen",
        email: "test@oneearth.io",
        dob: "1999-01-01",
        territory: "United States",
        flagEmoji: "🇺🇸",
        gender: "Male",
        currentRank: "Explorer",
        knowledgeCredits: 120,
        contributionCredits: 60,
        reputationScore: 98,
        personalityTraits: "Explorer,Builder,Creator",
        bio: "Dedicated pioneer testing our One Earth connection hub.",
        isCandidate: false,
        campaignVision: "",
        campaignManifesto: "",
        votesCount: 0,
        hasVoted: false,
        onboardingCompleted: true,
        citizenOathAccepted: true,
        followers: 10,
        following: 15,
        profilePhoto: "",
        passphrase: "password123",
        createdAt: Date.now(),
        updatedAt: Date.now()
      };
      await testUserRef.set(testUser);
      console.log('✅ Firestore: Test citizen created in Firestore.');
    }

    // 2. Initialize Sequence Counters
    const countersSnapshot = await db.collection('counters').limit(1).get();
    if (countersSnapshot.empty) {
      console.log('🌱 Firestore: Seeding incremental sequence counters...');
      await db.collection('counters').doc('post_id').set({ seq: 3 });
      await db.collection('counters').doc('comment_id').set({ seq: 3 });
      await db.collection('counters').doc('room_id').set({ seq: 0 });
      await db.collection('counters').doc('message_id').set({ seq: 0 });
      console.log('✅ Firestore: Sequence tables successfully synchronized.');
    }

    // 3. Seed Posts
    const postsSnapshot = await db.collection('posts').limit(1).get();
    if (postsSnapshot.empty) {
      console.log('🌱 Firestore: Seeding default administrative feeds...');
      const defaultPosts: IPost[] = [
        {
          id: 1,
          authorId: "arjun@oneearth.io",
          authorName: "Arjun Patel",
          authorUsername: "@arjun_vision",
          authorRank: "Royal Candidate",
          authorTerritory: "India",
          authorFlag: "🇮🇳",
          content: "We have completed the micro-reservoir blueprint. By shifting our daily action from empty popularity loops, we spent 40 hours building an irrigation system for 3 smallholder farms. Here is the open-access guide to local sand-dams.",
          category: "Article",
          timestamp: Date.now() - 3600000 * 24, // 1 day ago
          knowledgeValue: 18,
          contributionProof: 24,
          reputationImpact: 99,
          reactedWiseUsers: "",
          reactedHelpfulUsers: "",
          reactedInspiringUsers: ""
        },
        {
          id: 2,
          authorId: "clara@oneearth.io",
          authorName: "Clara Dupont",
          authorUsername: "@clara_sage",
          authorRank: "Guardian",
          authorTerritory: "France",
          authorFlag: "🇫🇷",
          content: "What is the collective responsibility of technological builders when designing attention architectures? We should explicitly reject arbitrary metric casinos in favor of qualitative dialogue focus. Join our structural inquiry.",
          category: "Inquiry",
          timestamp: Date.now() - 3600000 * 12, // 12 hours ago
          knowledgeValue: 35,
          contributionProof: 12,
          reputationImpact: 98,
          reactedWiseUsers: "",
          reactedHelpfulUsers: "",
          reactedInspiringUsers: ""
        },
        {
          id: 3,
          authorId: "kofi@oneearth.io",
          authorName: "Kofi Mensah",
          authorUsername: "@kofi_builder",
          authorRank: "Contributor",
          authorTerritory: "Kenya",
          authorFlag: "🇰🇪",
          content: "The solar micro-grid model for Lake Victoria fishing villages has successfully completed 120 run-hours. To combat grid-vulnerability, we implemented locally serviceable battery units. Looking for developers to join the telemetry code debaters.",
          category: "Debate",
          timestamp: Date.now() - 3600000 * 4, // 4 hours ago
          knowledgeValue: 15,
          contributionProof: 32,
          reputationImpact: 97,
          reactedWiseUsers: "",
          reactedHelpfulUsers: "",
          reactedInspiringUsers: ""
        }
      ];

      for (const p of defaultPosts) {
        await db.collection('posts').doc(String(p.id)).set(p);
      }
      console.log('✅ Firestore: Feeds successfully customized.');
    }

    // 4. Seed Comments
    const commentsSnapshot = await db.collection('comments').limit(1).get();
    if (commentsSnapshot.empty) {
      console.log('🌱 Firestore: Seeding default comments context...');
      const defaultComments: IComment[] = [
        {
          id: 1,
          postId: 1,
          authorName: "Clara Dupont",
          authorFlag: "🇫🇷",
          authorRank: "Guardian",
          content: "This sand-dam technique is spectacular Arjun. The physics of sediment water retention is pristine.",
          timestamp: Date.now() - 7000000
        },
        {
          id: 2,
          postId: 1,
          authorName: "Kofi Mensah",
          authorFlag: "🇰🇪",
          authorRank: "Contributor",
          content: "Can we customize these sand-dams for clay-heavy soil profiles? Let's check with some geologists in Kenya.",
          timestamp: Date.now() - 3000000
        },
        {
          id: 3,
          postId: 2,
          authorName: "Arjun Patel",
          authorFlag: "🇮🇳",
          authorRank: "Royal Candidate",
          content: "Indeed Clara. Depth of connection directly protects systemic human sanity.",
          timestamp: Date.now() - 10000000
        }
      ];

      for (const c of defaultComments) {
        await db.collection('comments').doc(String(c.id)).set(c);
      }
      console.log('✅ Firestore: Comments tables connected successfully.');
    }

  } catch (error) {
    console.error('⚠️ Firestore Seeding check failed:', error);
  }
};
