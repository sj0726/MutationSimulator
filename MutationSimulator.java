import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class MutationSimulator {
    public String[] acquiredMutations = new String[30];
    public int numAcquired = 0;
    public String c = "8FE604176A9EC25F6EF7CB455DCAB3A5";
    private String userName;
    private Double commonChance;
    private Double rareChance;
    private Double requiredChance;
    private Boolean allAcquired = false;
    private int maxRounds;
    private ArrayList<String> commonMutation = new ArrayList<String>(Arrays.asList("Icarus", "Through-Hole", "Elitist", "Money Power", "Craftsmanship", "HP Rob", "Bomb Hardening", "Claw Strengthening", "Skill Evolution", "Steel Skin", "Money Worm", "Enhance Recovery", "Hunting Instinct", "Increased Stamina", "Boxer", "Increase Ammo", "Transparent Reloading", "Focus Attack", "Booster", "Bullet Addition", "Explosive Bullets", "Fire Bullets", "Sharpshooter", "Sixth Sense", "Forced Fall", "Vaccine Grenade", "Bomb Backpack", "Bombardment Support", "Critical Hit", "Steel Bullet", "Shotgun Grenade", "Fireball", "Nitrogen Grenade", "Reinforced Genes", "Resist", "Steel Armor", "Flippers", "Steel Head", "Adaptability", "Elephant"));
    private ArrayList<String> rareMutation = new ArrayList<String>(Arrays.asList("Cheetah", "Kangaroo", "Double Jump", "Hero Appearance", "Fast Reload", "Contact Infection", "Bio-Bomb"));
    private ArrayList<String> requiredMutation = new ArrayList<String>(Arrays.asList("Enthusiasm", "Specialist", "Resurrection"));

    public MutationSimulator(String name, int numRounds) {
        userName = name;
        maxRounds = numRounds;
        commonChance = 0.8;
        rareChance = 0.1;
        requiredChance = 0.1;
    }

    public void run(String user, int roundNum) {
        if (numAcquired <= acquiredMutations.length) {
            mutationRNG(user, roundNum);
        }
        else {
            System.out.println(user + " has reached the max # of acquirable mutation!");
            System.out.println();
        }
    }

    public void mutationRNG(String user, int roundNum) {
        Random rng = new Random();
        Float mutationType = rng.nextFloat();

        // 80% initial chance of obtaining a common mutation, and 10% each for rare and required mutations initially as well
        if (commonChance == -1.0 && rareChance == -1.0 && requiredChance == -1.0) {
            allMutationsAcquired(user);
        }
        else if (mutationType <= commonChance) {
            // commonMutation

            // check if there is a common mutation obtainable
            if (commonMutation.size() > 0) {
                int mutationIndex = rng.nextInt(commonMutation.size());
                acquiredMutations[numAcquired] = commonMutation.remove(mutationIndex);
                System.out.println(user + " has obtained " + acquiredMutations[numAcquired] + "!");
                System.out.println();
                numAcquired++;
            }
            else {
                System.out.println(user + " has obtained all common mutations!");
                System.out.println("Searching for other mutations...");
                System.out.println();
                if (rareChance == -1.0 && requiredChance != -1.0) {
                    requiredChance = 1.0;
                }
                else if (requiredChance == -1.0 && rareChance != -1.0) {
                    rareChance = 1.0;
                }
                else if (requiredChance == rareChance && requiredChance != -1.0) {
                    requiredChance += commonChance;
                }
                else {
                    allMutationsAcquired(user);
                }
                commonChance = -1.0;
                mutationRNG(user, roundNum);
            }
        }
        else if (commonChance < mutationType && mutationType <= (commonChance + rareChance)) {
            // rareMutation

            // check if there is a rare mutation obtainable
            if (rareMutation.size() > 0) {
                int mutationIndex = rng.nextInt(rareMutation.size());
                acquiredMutations[numAcquired] = rareMutation.remove(mutationIndex);
                System.out.println(user + " has obtained " + acquiredMutations[numAcquired] + "!");
                System.out.println();
                numAcquired++;
            }
            else {
                System.out.println(user + " has obtained all rare mutations!");
                System.out.println("Searching for other mutations...");
                System.out.println();
                if (commonChance == -1.0 && requiredChance != -1.0) {
                    requiredChance = 1.0;
                }
                else if (requiredChance == -1.0 && commonChance != -1.0) {
                    commonChance = 1.0;
                }
                else if (requiredChance == commonChance && requiredChance != -1.0) {
                    commonChance += rareChance;
                }
                else {
                    allMutationsAcquired(user);
                }
                rareChance = -1.0;
                mutationRNG(user, roundNum);
            }
        }
        else {
            // requiredMutation

            // check if there is a required mutation obtainable
            if (requiredMutation.size() > 0) {
                int mutationIndex = rng.nextInt(requiredMutation.size());
                acquiredMutations[numAcquired] = requiredMutation.remove(mutationIndex);
                System.out.println(user + " has obtained " + acquiredMutations[numAcquired] + "!");
                System.out.println();
                numAcquired++;
    
                // every iteration of this RNG (every time a player obtains mutation point), lower the chance of common mutation by 10% and vice versa to obtaining required mutations.
                // this is to avoid the chance of players missing key mutations that have huge impact on the gameplay, thereby guaranteeing a relatively equal condition to all players at some point of the game.
                // by setting the chance of obtaining the required mutations as same as that of rare mutations, this algorithm avoids humans overpowering zombies in early stages of the game.
                // by round 9, there will be 90% chance of obtaining required mutations. on round 10, requiredMutation will take the chance of rare mutations as well for 100% chance of obtainage.
                if (commonChance > 0.0) {
                    commonChance -= 0.1;
                    requiredChance += 0.1;
                }
                else {
                    rareChance -= 0.1;
                    requiredChance += 0.1;
                }
            }
            else {
                System.out.println(user + " has obtained all required mutations!");
                System.out.println("Searching for other mutations...");
                System.out.println();

                if (commonChance == -1.0 && rareChance != -1.0) {
                    rareChance = 1.0;
                }
                else if (rareChance == -1.0 && commonChance != -1.0) {
                    commonChance = 1.0;
                }
                else if (rareChance == commonChance && rareChance != -1.0) {
                    commonChance += requiredChance;
                }
                else {
                    allMutationsAcquired(user);
                }
                requiredChance = -1.0;
                mutationRNG(user, roundNum);
            }
        }
    }

    public void allMutationsAcquired(String user) {
        System.out.println(user + " has obtained all mutations!");
        System.out.println();
        allAcquired = true;
    }

    public void startGame(String user, int numRounds) {
        Random rng = new Random();
        // to indicate at which round has the player acquired all mutations, if ever.
        // if not, have it initalized to the max number of rounds.
        int allObtainedRound = numRounds;

        for (int i = 1; i <= numRounds; i++) {
            // randomly generate the number of mutation points a player has earned in a round to simulate real gameplay environment.
            // will put higher weight on lower numbers using exponential distribution so it's unlikely that players obtain unrealistic amounts of points in a round.
            int numMutationEarned = 1;

            System.out.println("Round " + i);
            System.out.println("------------------");

            for (int j = 0; j < numMutationEarned; j++) {
                run(user, numRounds);

                if (allAcquired == true) {
                    break;
                }
            }

            if (allAcquired == true) {
                break;
            }
        }

        summary(user, allObtainedRound);
    }

    public void start() {
        startGame(this.userName, this.maxRounds);
    }

    public void summary(String user, int numRounds) {
        if (allAcquired == true) {
            System.out.println(user + " has acquired all mutations in " + numRounds + " rounds.");
        }
        else {
            System.out.println(user + " has acquired " + numAcquired + " mutations in " + numRounds + " rounds.");
        }
        System.out.println(user + "'s mutations: " + Arrays.toString(acquiredMutations));
        System.out.println();
    }

    public static void main(String[] args) {
        String name = args[0];
        int numRounds = Integer.parseInt(args[1]);
        MutationSimulator simulator = new MutationSimulator(name, numRounds);
        simulator.start();
    }
}