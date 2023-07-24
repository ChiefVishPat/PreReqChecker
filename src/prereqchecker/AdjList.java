package prereqchecker;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then 
 *    listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }

	    // // WRITE YOUR CODE HERE
        Schedule sched = new Schedule();
        String fileIn = args[0];
        String fileOut = args[1];

        sched.makeAdjList(sched, fileIn, fileOut);


    }   //end of main method
}


// StdIn.setFile(args[0]);
// StdOut.setFile(args[1]);

// Schedule schedule = new Schedule();
// int length = StdIn.readInt();

// for(int i = 0; i < length; i++){ //adds all the course keys into hashmap
// String id = StdIn.readString();
// schedule.addCourse(id);
// }

// length = StdIn.readInt();

// for(int i = 0; i < length; i++){ //adds pre-reqs into hashmap
// String id = StdIn.readString();
// String req = StdIn.readString();

// schedule.addPreReq(id, req);
// }

// String courseAndReqs = null; //build a string of the course + prereqs
// for(String course : schedule.getCourseHashList().keySet()){
// courseAndReqs = course + " ";
// ArrayList<String> preReqs = schedule.getCourseReqsList(course);
// for(String req : preReqs){
// courseAndReqs += req + " ";
// }
// StdOut.println(courseAndReqs + " ");
// }