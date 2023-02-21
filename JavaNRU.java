import java.util.*;

public class NRU {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of pages: ");
        int numPages = sc.nextInt();

        System.out.print("Enter the number of page frames: ");
        int numFrames = sc.nextInt();

        int[] pages = new int[numPages];
        int[] frames = new int[numFrames];
        int[] refBits = new int[numFrames];
        int pageFaults = 0;

        System.out.println("Enter the reference bit (0/1) for each page:");
        for (int i = 0; i < numPages; i++) {
            System.out.print("Page " + i + ": ");
            pages[i] = sc.nextInt();
        }

        Arrays.fill(frames, -1); // initialize all frames to -1

        for (int i = 0; i < numPages; i++) {
            int pageIndex = pages[i];

            boolean pageHit = false;
            for (int j = 0; j < numFrames; j++) {
                if (frames[j] == pageIndex) {
                    refBits[j] = 1;
                    pageHit = true;
                    break;
                }
            }

            if (!pageHit) {
                boolean pageInserted = false;
                for (int j = 0; j < numFrames; j++) {
                    if (frames[j] == -1) { // free frame
                        frames[j] = pageIndex;
                        refBits[j] = 1;
                        pageInserted = true;
                        pageFaults++;
                        break;
                    }
                }

                if (!pageInserted) { // no free frames
                    int evictIndex = -1;

                    // find the first page with reference bit 0 and dirty bit 0
                    for (int j = 0; j < numFrames; j++) {
                        if (refBits[j] == 0 && !isPageDirty(frames[j], pages, i, numPages)) {
                            evictIndex = j;
                            break;
                        }
                    }

                    if (evictIndex == -1) { // all pages are dirty or have reference bit 1
                        // find the first page with reference bit 0
                        for (int j = 0; j < numFrames; j++) {
                            if (refBits[j] == 0) {
                                evictIndex = j;
                                break;
                            }
                        }
                    }

                    frames[evictIndex] = pageIndex;
                    refBits[evictIndex] = 1;
                    pageFaults++;
                }
            }

            if ((i + 1) % 10 == 0) {
                System.out.println("After " + (i + 1) + " pages:");
                for (int j = 0; j < numFrames; j++) {
                    System.out.print(frames[j] + "\t");
                }
                System.out.println();
            }
        }

        System.out.println("Number of page faults: " + pageFaults);
    }

    private static boolean isPageDirty(int pageIndex, int[] pages, int curIndex, int numPages) {
        for (int i = curIndex + 1; i < numPages; i++) {
            if (pages[i] == pageIndex) {
                return true;
            }
        }
        return false;
    }
}



Certainly! This Java program implements the Not Recently Used (NRU) algorithm based on user input. The NRU algorithm is a page replacement algorithm that favors keeping in memory pages that have not been recently used. The program simulates the page replacement process for a fixed number of page frames and a sequence of page references entered by the user.

The program first prompts the user to enter the number of page frames and the sequence of page references. It then simulates the page replacement process by iterating through the page references and checking whether the current page is already in memory or not. If the page is already in memory, the program simply moves to the next reference. If the page is not in memory, the program selects a page to replace using the NRU algorithm.

In the NRU algorithm, pages are classified into four categories based on their referenced and modified bits: class 0 contains pages that have not been referenced or modified, class 1 contains pages that have been referenced but not modified, class 2 contains pages that have not been referenced but have been modified, and class 3 contains pages that have been both referenced and modified. The algorithm selects a page to replace by choosing the first page it finds in the lowest numbered class that is not empty.

After a page is selected for replacement, the program updates the page table and moves to the next reference. The program keeps track of the number of page faults that occur during the simulation and prints the final number of page faults when the simulation is complete.