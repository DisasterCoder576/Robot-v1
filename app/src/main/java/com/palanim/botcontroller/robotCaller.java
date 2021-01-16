package com.palanim.botcontroller;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class robotCaller extends AppCompatActivity {

    ListView stops;
    TextView targetView;
    String finalTarget;

    String arenaOP = null;
    int SIZE=5;
    int[][] OGMatint =    {    {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
            {  0,  0,  0,  0,  0},
    };
    int a=0;
    HashMap<String, List<Integer>> OGstopsint = new HashMap<String, List<Integer>>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_caller);


        targetView=(TextView)findViewById(R.id.target);



        /*

        --------------------DOCUMENTATION TEXT-------------------------
            \/\/\/\/ READING ARENA MATRIX FROM INTERNAL FILE STORAGE \/\/\/\/

            File name is arenaMatrix

            If file is not found, Click Setup Arena First

            File is not setup here. Only read.

        */

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         * TRY TO OPEN ArenaMatrix File from internal Storage
         *
         *

         *
         * ******
         */
        String filename = "arenaMatrix";


        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput(filename);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    Log.i("LINES", line.toString() );
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
                Log.i("mine1", "Arena File found" );
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {

                String contents = stringBuilder.toString();
                Log.i("mine1222222", "Arena File found" +contents );

                String[] lines = contents.split(System.getProperty("line.separator"));

                for (int i=0; i< lines.length; i++){
                    Log.i("mine1222222333333", "Arena File found" + lines[i] );
                    int[] result = Arrays.stream(lines[i].split(" ")).mapToInt(Integer::parseInt).toArray();
                    for(int j=0; j<result.length; j++){
                        OGMatint[i][j]=result[j];
                    }
                }
                /*
                for(int i=0; i<SIZE; i++ ){
                    for(int j=0; j<SIZE; j++){
                        Log.i("nums", Integer.toString(OGMatint[i][j]) + i+j);
                    }
                }*/



            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("mine1", " Arena File not found");



        }


        /*

        ****** DEVELOPER NOTICE
        Code that follows will read arenaStops and store it as a hashmap on global variable


         */

        /*
         ******
         *
         *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
         *
         *  Try to read Stops List from internal Storage
         *  File name is     arenaStops
         *
         * This happens every time app is installed.
         * The same file is read and modified again when needed in other activities
         *
         * ******
         */

        String filenameStops = "arenaStops";


        FileInputStream fis1 = null;
        try {
            fis1 = getApplicationContext().openFileInput(filenameStops);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis1, StandardCharsets.UTF_8);
            StringBuilder stringBuilder1 = new StringBuilder();
            try (BufferedReader reader1 = new BufferedReader(inputStreamReader)) {
                String line = reader1.readLine();
                while (line != null) {
                    stringBuilder1.append(line).append('\n');
                    line = reader1.readLine();
                }
                Log.i("mine1", "Arena File found" );
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {
                String contents = stringBuilder1.toString();
                Log.i("mineStops", "Arena Stops File found" + contents );

                String value = contents;
                value = value.substring(1, value.length()-1);           //remove curly brackets
                String[] keyValuePairs = value.split(", _");              //split the string to create key-value pairs
                HashMap<String, List<Integer>> map = new HashMap<>();

                for(String pair : keyValuePairs)                        //iterate over the pairs
                {
                    String[] entry = pair.split("=");            //split the pairs to get key and value
                    Log.i("hashu", entry[0] +"---"+ entry[1]);
                    String value1= entry[1];
                    Log.i("lenght", Integer.toString(value1.length()));
                    value1 = value1.substring(1, value1.length()-1);
                    Log.i("coords", value1.toString());//remove curly brackets
                    String[] coords = value1.split(", ");
                    coords[1] = coords[1].replace("{","");
                    coords[1] = coords[1].replace("}","");
                    coords[1] = coords[1].replace("]","");
                    coords[1] = coords[1].replace("[","");
                    Log.i("coords", coords.toString());
                    List<Integer> myList = new ArrayList<Integer>();
                    myList.add(Integer.parseInt(coords[0]));
                    myList.add(Integer.parseInt(coords[1]));
                    String toPut;
                    char comp = '_';
                    if( entry[0].charAt(0) != '_')
                        toPut="_"+ (entry[0].trim());
                    else
                        toPut=entry[0].trim();
                    OGstopsint.put(toPut, myList);          //add them to the hashmap and trim whitespaces
                    Log.i("Read stops as Hash", OGstopsint.toString());
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("mine1", " Arena File not found");




        }
        ArrayList<String> NewStops = new ArrayList<>();
        for(String i : OGstopsint.keySet()){
            NewStops.add(i);

        }

        stops = (ListView)findViewById(R.id.stops);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , NewStops);
        stops.setAdapter(arrayAdapter);

        stops.setOnItemClickListener(this::onItemClick);
        };

    static class Node implements Comparable {
        public AStar.Node parent;
        public int x, y;
        public double g;
        public double h;
        Node(AStar.Node parent, int xpos, int ypos, double g, double h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }
        // Compare by f value (g + h)
        @Override
        public int compareTo(Object o) {
            AStar.Node that = (AStar.Node) o;
            return (int)((this.g + this.h) - (that.g + that.h));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Log.i("Target",      parent.getItemAtPosition(position).toString()   );
        targetView.setText(parent.getItemAtPosition(position).toString());

        finalTarget =  parent.getItemAtPosition(position).toString();
    }

    /*
    -----------DOCUMENTATION------------

    \/\/\/\/ MAIN FUCNTION that finds path when clicked \/\/\/\/




     */


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GO(View v){
        int xend =0;
        int yend =0;
        List<Integer> myList12 = new ArrayList<Integer>();
        myList12=OGstopsint.get(finalTarget);
        xend=myList12.get(0);
        yend=myList12.get(1);
        AStar as = new AStar(OGMatint, 0, 0, false);
        List<AStar.Node> path = as.findPathTo(xend, yend);
        String finalpath ="";

        if (path != null) {
            for(int ii=0; ii<path.size(); ii++ ){
                finalpath += ("[" + path.get(ii).x + ", " + path.get(ii).y + "] ");
            }

        }
        Log.i("path",finalpath);


    }






    }




/*
 ******
 *--------------------------DOCUMENTATION TEXT -------------------
 *        \/\/\/\/      IMPORTANT NOTICE TO DEV/ READER          \/\/\/\/
 *
 *
 * Below Function is the A* Algorithm that calculates path from target!
 * roosettacode.org has credits in developing this code
 *
 * ******
 */

class AStar {
    private final List<Node> open;
    private final List<Node> closed;
    private final List<Node> path;
    private final int[][] maze;
    private Node now;
    private final int xstart;
    private final int ystart;
    private int xend, yend;
    private final boolean diag;

    // Node class for convienience
    static class Node implements Comparable {
        public Node parent;
        public int x, y;
        public double g;
        public double h;
        Node(Node parent, int xpos, int ypos, double g, double h) {
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }
        // Compare by f value (g + h)
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int)((this.g + this.h) - (that.g + that.h));
        }
    }

    AStar(int[][] maze, int xstart, int ystart, boolean diag) {
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.maze = maze;
        this.now = new Node(null, xstart, ystart, 0, 0);
        this.xstart = xstart;
        this.ystart = ystart;
        this.diag = diag;
    }
    /*
     ** Finds path to xend/yend or returns null
     **
     ** @param (int) xend coordinates of the target position
     ** @param (int) yend
     ** @return (List<Node> | null) the path
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Node> findPathTo(int xend, int yend) {
        this.xend = xend;
        this.yend = yend;
        this.closed.add(this.now);
        addNeigborsToOpenList();
        while (this.now.x != this.xend || this.now.y != this.yend) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.xstart || this.now.y != this.ystart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }
    /*
     ** Looks in a given List<> for a node
     **
     ** @return (bool) NeightborInListFound
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean findNeighborInList(List<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }
    /*
     ** Calulate distance between this.now and xend/yend
     **
     ** @return (int) distance
     */
    private double distance(int dx, int dy) {
        if (this.diag) { // if diagonal movement is alloweed
            return Math.hypot(this.now.x + dx - this.xend, this.now.y + dy - this.yend); // return hypothenuse
        } else {
            return Math.abs(this.now.x + dx - this.xend) + Math.abs(this.now.y + dy - this.yend); // else return "Manhattan distance"
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNeigborsToOpenList() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!this.diag && x != 0 && y != 0) {
                    continue; // skip if diagonal movement is not allowed
                }
                node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
                if ((x != 0 || y != 0) // not this.now
                        && this.now.x + x >= 0 && this.now.x + x < this.maze[0].length // check maze boundaries
                        && this.now.y + y >= 0 && this.now.y + y < this.maze.length
                        && this.maze[this.now.y + y][this.now.x + x] != -1 // check if square is walkable
                        && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                    node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                    node.g += maze[this.now.y + y][this.now.x + x]; // add movement cost for this square

                    // diagonal cost = sqrt(hor_cost² + vert_cost²)
                    // in this example the cost would be 12.2 instead of 11
                        /*
                        if (diag && x != 0 && y != 0) {
                            node.g += .4;	// Diagonal movement cost = 1.4
                        }
                        */
                    this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }
/*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        // -1 = blocked
        // 0+ = additional movement cost
        int[][] maze = {
                {  0,  0,  0,  0,  0,  0,  0,  0},
                {  0,  0,  0,  0,  0,  0,  0,  0},
                {  0,  0,  0,100,100,100,  0,  0},
                {  0,  0,  0,  0,  0,100,  0,  0},
                {  0,  0,100,  0,  0,100,  0,  0},
                {  0,  0,100,  0,  0,100,  0,  0},
                {  0,  0,100,100,100,100,  0,  0},
                {  0,  0,  0,  0,  0,  0,  0,  0},
        };
        AStar as = new AStar(maze, 0, 0, true);
        List<Node> path = as.findPathTo(7, 7);
        if (path != null) {
            path.forEach((n) -> {
                System.out.print("[" + n.x + ", " + n.y + "] ");
                maze[n.y][n.x] = -1;
            });
            System.out.printf("\nTotal cost: %.02f\n", path.get(path.size() - 1).g);

            for (int[] maze_row : maze) {
                for (int maze_entry : maze_row) {
                    switch (maze_entry) {
                        case 0:
                            System.out.print("_");
                            break;
                        case -1:
                            System.out.print("*");
                            break;
                        default:
                            System.out.print("#");
                    }
                }
                System.out.println();
            }
        }
    } */
}
