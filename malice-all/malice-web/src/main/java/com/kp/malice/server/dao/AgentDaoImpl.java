package com.kp.malice.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import com.google.inject.Inject;
import com.kp.malice.server.model.Agency;
import com.kp.malice.server.model.Agent;
import com.kp.malice.shared.proxies.AgencyProxy;

public class AgentDaoImpl implements AgentDao {

    private static Logger log = Logger.getLogger(AgentDaoImpl.class);

    private static final String[] FEMALE_FIRST_NAMES = { "Mary", "Patricia", "Linda", "Barbara", "Elizabeth",
            "Jennifer", "Maria", "Susan", "Margaret", "Dorothy", "Lisa", "Nancy", "Karen", "Betty", "Helen", "Sandra",
            "Donna", "Carol", "Ruth", "Sharon", "Michelle", "Laura", "Sarah", "Kimberly", "Deborah", "Jessica",
            "Shirley", "Cynthia", "Angela", "Melissa", "Brenda", "Amy", "Anna", "Rebecca", "Virginia", "Kathleen",
            "Pamela", "Martha", "Debra", "Amanda", "Stephanie", "Carolyn", "Christine", "Marie", "Janet", "Catherine",
            "Frances", "Ann", "Joyce", "Diane", "Alice", "Julie", "Heather", "Teresa", "Doris", "Gloria", "Evelyn",
            "Jean", "Cheryl", "Mildred", "Katherine", "Joan", "Ashley", "Judith", "Rose", "Janice", "Kelly", "Nicole",
            "Judy", "Christina", "Kathy", "Theresa", "Beverly", "Denise", "Tammy", "Irene", "Jane", "Lori", "Rachel",
            "Marilyn", "Andrea", "Kathryn", "Louise", "Sara", "Anne", "Jacqueline", "Wanda", "Bonnie", "Julia", "Ruby",
            "Lois", "Tina", "Phyllis", "Norma", "Paula", "Diana", "Annie", "Lillian", "Emily", "Robin", "Peggy",
            "Crystal", "Gladys", "Rita", "Dawn", "Connie", "Florence", "Tracy", "Edna", "Tiffany", "Carmen", "Rosa",
            "Cindy", "Grace", "Wendy", "Victoria", "Edith", "Kim", "Sherry", "Sylvia", "Josephine", "Thelma",
            "Shannon", "Sheila", "Ethel", "Ellen", "Elaine", "Marjorie", "Carrie", "Charlotte", "Monica", "Esther",
            "Pauline", "Emma", "Juanita", "Anita", "Rhonda", "Hazel", "Amber", "Eva", "Debbie", "April", "Leslie",
            "Clara", "Lucille", "Jamie", "Joanne", "Eleanor", "Valerie", "Danielle", "Megan", "Alicia", "Suzanne",
            "Michele", "Gail", "Bertha", "Darlene", "Veronica", "Jill", "Erin", "Geraldine", "Lauren", "Cathy",
            "Joann", "Lorraine", "Lynn", "Sally", "Regina", "Erica", "Beatrice", "Dolores", "Bernice", "Audrey",
            "Yvonne", "Annette", "June", "Samantha", "Marion", "Dana", "Stacy", "Ana", "Renee", "Ida", "Vivian",
            "Roberta", "Holly", "Brittany", "Melanie", "Loretta", "Yolanda", "Jeanette", "Laurie", "Katie", "Kristen",
            "Vanessa", "Alma", "Sue", "Elsie", "Beth", "Jeanne" };

    private static final String[] LAST_NAMES = { "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
            "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson",
            "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall", "Allen", "Young",
            "Hernandez", "King", "Wright", "Lopez", "Hill", "Scott", "Green", "Adams", "Baker", "Gonzalez", "Nelson",
            "Carter", "Mitchell", "Perez", "Roberts", "Turner", "Phillips", "Campbell", "Parker", "Evans", "Edwards",
            "Collins", "Stewart", "Sanchez", "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell", "Murphy", "Bailey",
            "Rivera", "Cooper", "Richardson", "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray", "Ramirez",
            "James", "Watson", "Brooks", "Kelly", "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross", "Henderson",
            "Coleman", "Jenkins", "Perry", "Powell", "Long", "Patterson", "Hughes", "Flores", "Washington", "Butler",
            "Simmons", "Foster", "Gonzales", "Bryant", "Alexander", "Russell", "Griffin", "Diaz", "Hayes", "Myers",
            "Ford", "Hamilton", "Graham", "Sullivan", "Wallace", "Woods", "Cole", "West", "Jordan", "Owens",
            "Reynolds", "Fisher", "Ellis", "Harrison", "Gibson", "Mcdonald", "Cruz", "Marshall", "Ortiz", "Gomez",
            "Murray", "Freeman", "Wells", "Webb", "Simpson", "Stevens", "Tucker", "Porter", "Hunter", "Hicks",
            "Crawford", "Henry", "Boyd", "Mason", "Morales", "Kennedy", "Warren", "Dixon", "Ramos", "Reyes", "Burns",
            "Gordon", "Shaw", "Holmes", "Rice", "Robertson", "Hunt", "Black", "Daniels", "Palmer", "Mills", "Nichols",
            "Grant", "Knight", "Ferguson", "Rose", "Stone", "Hawkins", "Dunn", "Perkins", "Hudson", "Spencer",
            "Gardner", "Stephens", "Payne", "Pierce", "Berry", "Matthews", "Arnold", "Wagner", "Willis", "Ray",
            "Watkins", "Olson", "Carroll", "Duncan", "Snyder", "Hart", "Cunningham", "Bradley", "Lane", "Andrews",
            "Ruiz", "Harper", "Fox", "Riley", "Armstrong", "Carpenter", "Weaver", "Greene", "Lawrence", "Elliott",
            "Chavez", "Sims", "Austin", "Peters", "Kelley", "Franklin", "Lawson" };

    private static final String[] MALE_FIRST_NAMES = { "James", "John", "Robert", "Michael", "William", "David",
            "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
            "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary",
            "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory",
            "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur",
            "Ryan", "Roger", "Joe", "Juan", "Jack", "Albert", "Jonathan", "Justin", "Terry", "Gerald", "Keith",
            "Samuel", "Willie", "Ralph", "Lawrence", "Nicholas", "Roy", "Benjamin", "Bruce", "Brandon", "Adam",
            "Harry", "Fred", "Wayne", "Billy", "Steve", "Louis", "Jeremy", "Aaron", "Randy", "Howard", "Eugene",
            "Carlos", "Russell", "Bobby", "Victor", "Martin", "Ernest", "Phillip", "Todd", "Jesse", "Craig", "Alan",
            "Shawn", "Clarence", "Sean", "Philip", "Chris", "Johnny", "Earl", "Jimmy", "Antonio", "Danny", "Bryan",
            "Tony", "Luis", "Mike", "Stanley", "Leonard", "Nathan", "Dale", "Manuel", "Rodney", "Curtis", "Norman",
            "Allen", "Marvin", "Vincent", "Glenn", "Jeffery", "Travis", "Jeff", "Chad", "Jacob", "Lee", "Melvin",
            "Alfred", "Kyle", "Francis", "Bradley", "Jesus", "Herbert", "Frederick", "Ray", "Joel", "Edwin", "Don",
            "Eddie", "Ricky", "Troy", "Randall", "Barry", "Alexander", "Bernard", "Mario", "Leroy", "Francisco",
            "Marcus", "Micheal", "Theodore", "Clifford", "Miguel", "Oscar", "Jay", "Jim", "Tom", "Calvin", "Alex",
            "Jon", "Ronnie", "Bill", "Lloyd", "Tommy", "Leon", "Derek", "Warren", "Darrell", "Jerome", "Floyd", "Leo",
            "Alvin", "Tim", "Wesley", "Gordon", "Dean", "Greg", "Jorge", "Dustin", "Pedro", "Derrick", "Dan", "Lewis",
            "Zachary", "Corey", "Herman", "Maurice", "Vernon", "Roberto", "Clyde", "Glen", "Hector", "Shane",
            "Ricardo", "Sam", "Rick", "Lester", "Brent", "Ramon", "Charlie", "Tyler", "Gilbert", "Gene" };

    private static final String[] STREET_NAMES = { "Peachtree", "First", "Second", "Third", "Fourth", "Fifth", "Sixth",
            "Tenth", "Fourteenth", "Spring", "Techwood", "West Peachtree", "Juniper", "Cypress", "Fowler", "Piedmont",
            "Juniper", "Main", "Central", "Currier", "Courtland", "Williams", "Centennial", "Olympic", "Baker",
            "Highland", "Pryor", "Decatur", "Bell", "Edgewood", "Mitchell", "Forsyth", "Capital" };

    private static final String[] STREET_SUFFIX = { "St", "Rd", "Ln", "Blvd", "Way", "Pkwy", "Cir", "Ave" };

    /**
     * Create a new random {@link Agent}.
     * @param i 
     * 
     * @return the new {@link Agent}.
     */
    @SuppressWarnings("deprecation")
    public Agent createAgent() {
        Agent agent = new Agent();
        agent.setAgency(nextValueList(queryAgenciesPerPopolareDataProvider()));
        agent.setSurname(nextValue(LAST_NAMES));
        if (new Random().nextBoolean()) {
            // Male.
            agent.setName(nextValue(MALE_FIRST_NAMES));
        } else {
            // Female.
            agent.setName(nextValue(FEMALE_FIRST_NAMES));
        }
        agent.setRole("ROLE_AGENTE");
        agent.setId((long) (int) (Math.random() * 100000));
        return agent;
    }

    @Override
    public List<Agent> loadAgents() {
        List<Agent> agents = new ArrayList<Agent>();
        for (int i = 0; i < 20; i++) {
            agents.add(createAgent());
        }
        return agents;
    }

    public List<Agency> queryAgenciesPerPopolareDataProvider() {
        List<Agency> agencies = new ArrayList<Agency>();
        String[] catNames = new String[] { "Family", "Friends", "Coworkers", "Businesses", "Agents" };
        for (int i = 0; i < catNames.length; i++) {
            Agency agency = new Agency(catNames[i]);
            agency.setId((long) i);
            agencies.add(agency);
        }
        return AgencyDaoImpl.agencies;
    }

    /**
     * Get the next random value from an array.
     * 
     * @param array
     *            the array
     * @return a random value in the array
     */
    private <T> T nextValue(T[] array) {
        return array[new Random().nextInt(array.length)];
    }

    private <T> T nextValueList(List<T> agencies) {
        int i = new Random().nextInt(agencies.size());
        return agencies.get(i);
    }

    @Override
    public void deleteAgent(Agent agent) {
        log.debug("agent deleted");
    }

    @Override
    public void persist(Agent agent) {
        encriptPassword(agent); //la password va criptata prima di salvarla a db
        log.debug("agent persisted");
    }

    private void encriptPassword(Agent agent) {
        agent.setPassword(BCrypt.hashpw(agent.getPassword(), BCrypt.gensalt()));
    }

}
