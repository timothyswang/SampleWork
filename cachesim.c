//Timothy Wang
//wangtimo@usc.edu
//Assistance from Yuxi (Andy) on general overview of assignemnt and for the parse_arguments function (10/23/2020)
#include <getopt.h>  // getopt, optarg
#include <stdlib.h>  // exit, atoi, malloc, free
#include <stdio.h>   // printf, fprintf, stderr, fopen, fclose, FILE
#include <limits.h>  // ULONG_MAX
#include <string.h>  // strcmp, strerror
#include <errno.h>   // errno
#include <stdbool.h> // boolean
#include <limits.h> // limits
#include <math.h> // exponentiation

/* fast base-2 integer logarithm */
#define INT_LOG2(x) (31 - __builtin_clz(x))
#define NOT_POWER2(x) (__builtin_clz(x) + __builtin_ctz(x) != 31)

/* tag_bits = ADDRESS_LENGTH - set_bits - block_bits */
#define ADDRESS_LENGTH 64

/**
 * Print program usage (no need to modify).
 */
static void print_usage() {
    printf("Usage: csim [-hv] -S <num> -K <num> -B <num> -p <policy> -t <file>\n");
    printf("Options:\n");
    printf("  -h           Print this help message.\n");
    printf("  -v           Optional verbose flag.\n");
    printf("  -S <num>     Number of sets.           (must be > 0)\n");
    printf("  -K <num>     Number of lines per set.  (must be > 0)\n");
    printf("  -B <num>     Number of bytes per line. (must be > 0)\n");
    printf("  -p <policy>  Eviction policy. (one of 'FIFO', 'LRU')\n");
    printf("  -t <file>    Trace file.\n\n");
    printf("Examples:\n");
    printf("  $ ./csim    -S 16  -K 1 -B 16 -p LRU -t traces/yi.trace\n");
    printf("  $ ./csim -v -S 256 -K 2 -B 16 -p LRU -t traces/yi.trace\n");
}

/* Parameters set by command-line args (no need to modify) */
int verbose = 0;   // print trace if 1
int S = 0;         // number of sets
int K = 0;         // lines per set
int B = 0;         // bytes per line

typedef enum { FIFO = 1, LRU = 2 } Policy;
Policy policy;     // 0 (undefined) by default

FILE *trace_fp = NULL;

/**
 * Parse input arguments and set verbose, S, K, B, policy, trace_fp.
 *
 * TODO: Finish implementation
 */
static void parse_arguments(int argc, char **argv) {
    char c;
    while ((c = getopt(argc, argv, "S:K:B:p:t:vh")) != -1) {
        switch(c) {
            case 'S':
                S = atoi(optarg);
                if (NOT_POWER2(S)) {
                    fprintf(stderr, "ERROR: S must be a power of 2\n");
                    exit(1);
                }
                break;
            case 'K':
                K = atoi(optarg);

                //Check if K <= 0 Quit
                if (K == 0){
                    exit(1);
                }
                // TODO
                break;
            case 'B':
                B = atoi(optarg);
                if (NOT_POWER2(B)) {
                    fprintf(stderr, "ERROR: B must be a power of 2\n");
                    exit(1);
                }
                // TODO
                break;
            case 'p':
                if (!strcmp(optarg, "FIFO")) {
                    policy = FIFO;
                }
                else if (!strcmp(optarg, "LRU")) {
                    policy = LRU;
                }
                else {
                    fprintf(stderr, "ERROR: Unknown policy");
                    exit(1);
                }
                // TODO: parse LRU, exit with error for unknown policy

                //Check if LRU

                break;
            case 't':
                // TODO: open file trace_fp for reading

                // C function to read in file


                trace_fp = fopen(optarg, "r");

                if (!trace_fp) {
                    fprintf(stderr, "ERROR: %s: %s\n", optarg, strerror(errno));
                    exit(1);
                }


                break;
            case 'v':

                // TODO

                // Prints what the trace is supposed to do and at what given address

                // Boolean variable set to true

                // Reprints what's in the trace. Adds in information about
                // whether it's a miss, an eviction, a hit, etc.

                verbose = 1;


                break;
            case 'h':
                // TODO

                //How to use program


                exit(0);
            default:
                print_usage();
                exit(1);
        }
    }

    /* Make sure that all required command line args were specified and valid */
    if (S <= 0 || K <= 0 || B <= 0 || policy == 0 || !trace_fp) {
        printf("ERROR: Negative or missing command line arguments\n");
        print_usage();
        if (trace_fp) {
            fclose(trace_fp);
        }
        exit(1);
    }

    /* Other setup if needed */



}

/**
 * Cache data structures
 * TODO: Define your own!
 */

 // Dynamically allocated 2D Array (set and line) of Cache Entrys
 // i --> set, j --> line, 
 typedef struct {
    //Valid Bit
    bool valid;
    //How many times this entry has been used
    int numUsed;
    //Tag bits
    int tag;
 } CacheEntry;

 //Statically declare 2D array of Cache Entry
 typedef CacheEntry** Cache;

 Cache myCache;

 // Array for LRU --> number of sets. Contains current LRU count
 // FIFO Array for counters --> number of sets. Contains current FIFO count
 int* LRU_array;
 int* FIFO_array;

 //Variables for operation, and size
 char command;
 unsigned int size;

/**
 * Allocate cache data structures.
 *
 * This function dynamically allocates (with malloc) data structures for each of
 * the `S` sets and `K` lines per set.
 *
 * TODO: Implement
 */
static void allocate_cache() {

    //Allocate for Cache
    myCache = malloc(S * sizeof(CacheEntry*));

    for (int i = 0; i < S; i++){
        myCache[i] = malloc(K * sizeof(CacheEntry));
    }

    //Allocate LRU_array
    LRU_array = malloc(S * sizeof(int));
    
    //Allocate FIFO_array
    FIFO_array = malloc(S * sizeof(int));

}

/**
 * Deallocate cache data structures.
 *
 * This function deallocates (with free) the cache data structures of each
 * set and line.
 *
 * TODO: Implement
 */
static void free_cache() {

    for (int i = 0; i < S; i++){
        free(myCache[i]);
    }

    free(myCache);

    free(LRU_array);

    free(FIFO_array);

}

/* Counters used to record cache statistics */
int miss_count     = 0;
int hit_count      = 0;
int eviction_count = 0;

/**
 * Simulate a memory access.
 *
 * If the line is already in the cache, increase `hit_count`; otherwise,
 * increase `miss_count`; increase `eviction_count` if another line must be
 * evicted. This function also updates the metadata used to implement eviction
 * policies (LRU, FIFO).
 *
 * TODO: Implement
 */
static void access_data(unsigned long addr) {  
    if (verbose == true){
        printf("Access to %016lx\n", addr);
    }
    //Parse address

    //Define offsetSize and setSize
    int offsetSize = INT_LOG2(B);
    int setSize = INT_LOG2(S);

    // Calculate 2^(offsetSize) and 2^(setSize)
    int modOffset = 1;
    int modSet = 1;
    for (int i = 0; i < offsetSize; i++){
        modOffset = modOffset * 2;
    }
    for (int i = 0; i < setSize; i ++){
        modSet = modSet * 2;
    }

    //Remove offset from address
    addr = addr / modOffset;

    // mySet = addr mod modSet
    int mySet = addr % modSet;

    // myTag = addr / modSet
    int myTag = addr / modSet;

    //In the set, check if the tag matches up
    bool found = false;
    for (int i = 0; i < K; i++){
        //If hit

        if (myCache[mySet][i].tag == myTag && myCache[mySet][i].valid == true){
            //Update hit_count
            hit_count++;

            //Update num used
            //Increment the number in LRU_array[mySet] by 1
            LRU_array[mySet] = LRU_array[mySet] + 1;
            //Update myCache[mySet][i].numUsed
            myCache[mySet][i].numUsed = LRU_array[mySet];


            if (verbose == 1){
                printf("Hit ");
            }

            found = true;

            break;
            
        }
        
        
    }
    //If miss
    if (found == false){
        //Update miss_count
        miss_count++;
        if (verbose == 1){
            printf("Miss ");
        }

        //Check if eviction is needed
        bool evictionNeeded = true;
        int addLineHere = 0;
        for (int i = 0; i < K; i++){
            if (myCache[mySet][i].valid == false){
                //If there is an invalid line, there is no need to evict
                evictionNeeded = false;
                //Set addLineHere to i
                addLineHere = i;
                break;
            }
        }

        //Invoke eviction policy if eviction is needed
        if (evictionNeeded == true && policy == FIFO){
            int lineEvicted = FIFO_array[mySet];

            //Update FIFO_array
            FIFO_array[mySet]++;
            FIFO_array[mySet] = FIFO_array[mySet] % K;

            //Finish eviction
            myCache[mySet][lineEvicted].valid = false;
            //Set addLineHere to the line that was just evicted
            addLineHere = lineEvicted;
        }
        else if (evictionNeeded == true && policy == LRU){
            //Find line with the lowest numUsed value
            int lowestLine = 0;
            int lowestValue = INT_MAX;
            for (int i = 0; i < K; i++){
                if (myCache[mySet][i].numUsed < lowestValue){
                    lowestValue = myCache[mySet][i].numUsed;
                    lowestLine = i;
                }
            }

            int lineEvicted = lowestLine;

            //Finish eviction
            myCache[mySet][lineEvicted].valid = false;
            //Set addLineHere to the line that was just evicted
            addLineHere = lineEvicted;

        }

        if (evictionNeeded == true){
            //Update eviction_count
            eviction_count++;
            if (verbose == 1){
                printf("Eviction ");
            }
        }
        
        //Add memory to site. Update valid, tag, numUsed.
        myCache[mySet][addLineHere].valid = true;
        myCache[mySet][addLineHere].tag = myTag;


        //Update num used
        //Increment the number in LRU_array[mySet] by 1
        LRU_array[mySet] = LRU_array[mySet] + 1;
        //Update myCache[mySet][i].numUsed
        myCache[mySet][addLineHere].numUsed = LRU_array[mySet];
        

    }

    if (verbose == true){
        printf("\n");
    }
    

}

/**
 * Replay the input trace.
 *
 * This function:
 * - reads lines (e.g., using fgets) from the file handle `trace_fp` (a global variable)
 * - skips lines not starting with ` S`, ` L` or ` M`
 * - parses the memory address (unsigned long, in hex) and len (unsigned int, in decimal)
 *   from each input line
 * - calls `access_data(address)` for each access to a cache line
 *
 * TODO: Implement
 */
static void replay_trace() {

    //Update the LRU_array and the FIFO_array to be filled with zeroes

    for (int i = 0; i < S; i++){
        LRU_array[i] = 0;
        FIFO_array[i] = 0;
    }

    //Contains information for each line
    char myLine[4096];

    //For each line
    while ( fgets(myLine, 4096, trace_fp) != NULL){

        //printf("Hello2");

        //New value for myAddress
        unsigned int myAddress;

        //If we should read in line
        if (myLine[1] == 'S' || myLine[1] == 'L' || myLine[1] == 'M'){

            //Parse line
            sscanf(myLine, " %c %x,%u", &command, &myAddress, &size);

            //Call Access Data once if command is "S" or "L". Twice if "M"

            //Define offsetSize and setSize
            int offsetSize = INT_LOG2(B);
            int setSize = INT_LOG2(S);

            // Calculate 2^(offsetSize) and 2^(setSize)
            int modOffset = 1;
            int modSet = 1;
            for (int i = 0; i < offsetSize; i++){
                modOffset = modOffset * 2;
            }
            for (int i = 0; i < setSize; i ++){
                modSet = modSet * 2;
            }
            int myOffset = myAddress % modOffset;

            //Assistance from Shray Pungaliya on the "size" tests (10/28/2020)

            if (myLine[1] == 'M'){
                access_data(myAddress);
                access_data(myAddress);

                //If byte size is greater than 1, check if crosses boundary
                //If size + myOffset > size of line
                int mySum = size + myOffset;
                myAddress = ((myAddress / B) + 1) * B;
                //Include number of bytes in line
                while (mySum > B){

                    access_data(myAddress);
                    access_data(myAddress);

                    //Update myAddress
                    myAddress += B;

                    mySum = mySum - B;
                }

            }
            else{

                access_data(myAddress);

                //If byte size is greater than 1, check if crosses boundary
                //If size + myOffset > size of line
                int mySum = size + myOffset;
                myAddress = ((myAddress / B) + 1) * B;
                //Include number of bytes in line
                while (mySum > B){

                    access_data(myAddress);

                    //Update myAddress
                    myAddress += B;

                    mySum = mySum - B;
                }
            }

            //If verbose is on, print line
            if (verbose == 1){
                printf("%s", myLine);
                printf("\n");
            }

         }
    }
}

/**
 * Print cache statistics (DO NOT MODIFY).
 */
static void print_summary(int hits, int misses, int evictions) {
    printf("hits:%d misses:%d evictions:%d\n", hits, misses, evictions);
}

int main(int argc, char **argv) {
    parse_arguments(argc, argv);  // set global variables used by simulation
    allocate_cache();             // allocate data structures of cache
    replay_trace();               // simulate the trace and update counts
    free_cache();                 // deallocate data structures of cache
    fclose(trace_fp);             // close trace file
    print_summary(hit_count, miss_count, eviction_count);  // print counts
    return 0;
}
