import java.util.ArrayList;
import java.util.List;


public class Main
{
    private int greater(int a, int b)
    {
        if (a > b) return a;
        return b;
    }

    // given two skylines, merge them
    private List<int[]> mergeSkylines(List<int[]> skylineListLower, List<int[]> skylineListHigher)
    {
        int h1 = 0, h2 = 0;
        int newIndex = 0;
        List<int[]> skylineMerged = new ArrayList<int[]>();;

        while (true)
        {
            if (skylineListLower.isEmpty() || skylineListHigher.isEmpty()) //Check for empty case
            {
                break;
            }

            // first key points from both the skylines
            int [] stripe1 = skylineListLower.get(0);
            int [] stripe2 = skylineListHigher.get(0);


            int [] mergedStripe = new int[2]; //x-coordinate and height


            if (stripe1[0] < stripe2[0]) // comparing 'x' co-ordinates of current key points of skyline-1 and skyline-2
            {
                mergedStripe[0] = stripe1[0];
                mergedStripe[1] = stripe1[1];

                if (stripe1[1] < h2) //If y coordinate is smaller for the x-coornidate in case of overlap we update
                //merged key point's 'y' co-ordinate to last seen height of skyline-2
                {
                    mergedStripe[1] = h2;
                }

                h1 = stripe1[1]; //Updating last seen height of skyline-1

                // move to next key point for this skyline
                skylineListLower.remove(0);
            }
            else if (stripe2[0] < stripe1[0])
            {
                mergedStripe[0] = stripe2[0];
                mergedStripe[1] = stripe2[1];

                if (stripe2[1] < h1)
                {
                    mergedStripe[1] = h1;
                }

                h2 = stripe2[1]; //Updating last seen height of skyline-2

                skylineListHigher.remove(0);
            }

            else //If x coordinate is same then choosing the skyline with greater height
            {
                mergedStripe[0] = stripe2[0];
                mergedStripe[1] = greater(stripe1[1], stripe2[1]);

                h1 = stripe1[1];
                h2 = stripe2[1];

                skylineListLower.remove(0);
                skylineListHigher.remove(0);
            }

            skylineMerged.add(mergedStripe);
        }

        if (skylineListLower.isEmpty())
        {
            while (!skylineListHigher.isEmpty()) //Only Upper half left
            {
                skylineMerged.add(skylineListHigher.remove(0)); //Adding and removing
            }
        }
        else
        {
            while (!skylineListLower.isEmpty()) //Only lower half left
            {
                skylineMerged.add(skylineListLower.remove(0)); //Adding and removing
            }
        }

        int curr= 0; //Removes redundant key points from merged Skyline
        while (curr< skylineMerged.size())
        {
            boolean bool = true;
            int i = curr+ 1;
            while ((i < skylineMerged.size()) &&  bool)
            {
                if (skylineMerged.get(curr)[1] == skylineMerged.get(i)[1])
                {
                    bool = true;
                    skylineMerged.remove(i);
                }
                else
                {
                    bool = false;
                }
            }
            curr += 1;
        }

        return skylineMerged;
    }

    private List<int[]> divideskyline(int low, int high, int[][]buildings)
    {
        List<int[]> skyLineList = new ArrayList<int[]>();

        // invalid case
        if (low > high)
        {
            return skyLineList;
        }

        else if (low == high) //Only on building bring diagonal points
        {
            int x1 = buildings[low][0];
            int x2 = buildings[low][1];
            int h  = buildings[low][2];

            int[] element1 = {x1, h}; // Diagonal element
            int[] element2 = {x2, 0}; // Diagonal element

            skyLineList.add(element1);
            skyLineList.add(element2);

            return skyLineList;
        }
        // general case
        else
        {
            int mid = (low + high) / 2;

            List<int[]> skylineListLower = divideskyline(low, mid, buildings); // lower half

            List<int[]> skylineListHigher = divideskyline(mid+1, high, buildings); //upper half

            return mergeSkylines(skylineListLower, skylineListHigher); //Merge
        }
    }


    public static void main(String[] args)
    {
        int[][] buildings = {{1,9,10}, {2,6,15}, {3,12,11}, {9,16,10}, {13,16,9}, {14,17,5}};

        Main sskyline = new Main();

        List<int[]> skyLine = sskyline.divideskyline(0, buildings.length-1, buildings);

        System.out.println("Skyline for given buildings will be: ");
        for (int i = 0;  i < skyLine.size(); i++)
        {
            System.out.println(skyLine.get(i)[0]+","+skyLine.get(i)[1]);
        }
    }
}
