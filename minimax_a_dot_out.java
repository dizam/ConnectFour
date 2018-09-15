// Brian Becker, 999646986
// Daljodh Pannu, 912303549
 
public class minimax_a_dot_out extends AIModule {
 
    private int us = 1;
    private int them = 1;
    private int BestMove;
    private int[] normalBestOrder = {3, 4, 2, 5, 1, 6, 0};
    private int first = 1;
 
    private int[][] tableWeights = {
            {1, 2, 4, 6, 4, 2, 1},
            {2, 3, 7, 8, 7, 3, 2},
            {4, 7, 8, 9, 8, 7, 4},
            {4, 7, 8, 9, 8, 7, 4},
            {2, 3, 7, 8, 7, 3, 2},
            {1, 2, 4, 6, 4, 2, 1}
        };
 
    private int winWeight = 10000000;
    private int twoHorzWeight = 10000;
    private int threeDiagWeight = 1000;
    //private int threeHorzWeight = 1000;
 
 
    @Override
    public void getNextMove(final GameStateModule state) {
        for(int i : normalBestOrder) {
            if (state.canMakeMove(i)) {
                BestMove = i;
                break;
            } 
        }

        int finalDepth = 4;

        us = state.getActivePlayer();
        if (us == 1) them = 2;
 
        if (first == 1) {
            chosenMove = 3;
            first += 1;
        } //best opening move is always in the center
        else {
            while(!terminate) {
                 minimax_decision(state.copy(), 0, finalDepth);
     
                 if(!terminate) chosenMove = BestMove;
     
                 finalDepth += 1;
            } //iterative deepening, look farther and farther down tree until terminate
        }
    }
 
    // minimax_decision funciton as described in book
    private int minimax_decision(GameStateModule curState, int curDepth, int finalDepth) {
         
        return max_value(curState, curDepth, finalDepth);
    }
 
    private int min_value(GameStateModule testState, int curDepth, int finalDepth) {
 
        if (terminate) return 0;
        if (curDepth == finalDepth) return utility(testState, them);
 
        curDepth += 1;
        int v = Integer.MAX_VALUE;

        for(int i : normalBestOrder) {
            if (testState.canMakeMove(i)) {
 
                GameStateModule temp = testState.copy();
                temp.makeMove(i);

                v = Math.min(v, max_value(temp, curDepth, finalDepth));
            }  // move is available
        }
         
        return v;
    }
 
    private int max_value(GameStateModule testState, int curDepth, int finalDepth) {
         
        if (terminate) return 0;
        if (curDepth == finalDepth) return utility(testState, us);
 
        curDepth += 1;
        int v = Integer.MIN_VALUE;
 
        for(int i : normalBestOrder) {
            if (testState.canMakeMove(i)) {
                
                GameStateModule temp = testState.copy();
                temp.makeMove(i);

                int prev = v;

                v = Math.max(v, min_value(temp, curDepth, finalDepth));

                if (curDepth == 1 && v != prev) BestMove = i;
                 
            } // move is available
        }
 
        return v;
    }
    
    //evaluation function based on two in a row, three in a row diagonally, and 4 in a row (win)
    private int utility(GameStateModule testState, int player) {
        int ourWins = 0;
        int enemyWins = 0;
        int enemyWeight = 0;
        int ourWeight = 0;
        int ourTwoInRow = 0;
        int enemyTwoInRow = 0;

        int our3Diag = 0;
        int enemy3Diag = 0;

        // int our3Horz = 0;
        // int enemy3Horz = 0;

        int enemyPlayer = 1;
        if (player == 1) enemyPlayer = 2;
  
            for (int row = 0; row < 6; row ++) {  // start from bottom
 
                for (int col = 0; col < 7; col ++) {  // start from left
                        //System.out.print(testState.getAt(col, row) + " ");
 
                    if (testState.getAt(col, row) == player) ourWeight += tableWeights[row][col];
                    else if (testState.getAt(col, row) == enemyPlayer) enemyWeight += tableWeights[row][col];
 
                    if (row + 3 < 6) {  // if has 3 more spaces below
                        if (testState.getAt(col, row) == player && testState.getAt(col, row + 1) == player && 
                            testState.getAt(col, row + 2) == player && testState.getAt(col, row + 3) == player) ourWins += 1;
 
                        else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col, row + 1) == enemyPlayer && 
                            testState.getAt(col, row + 2) == enemyPlayer && testState.getAt(col, row + 3) == enemyPlayer) enemyWins += 1;
                         
                    }  // Check vertical 4 in a row
 
                    if (col > 0 &&  col + 2 < 7) {  // if has 2 more spaces to the right
                        if (testState.getAt(col, row) == player && testState.getAt(col + 1, row) == player && 
                            testState.getAt(col - 1, row) == 0 && testState.getAt(col + 2, row) == 0) ourTwoInRow += 1;
 
                        else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row) == enemyPlayer && 
                            testState.getAt(col - 1, row) == 0 && testState.getAt(col + 2, row) == 0) enemyTwoInRow += 1;
                    }  // check horizontal 2 in row
 

                    if (col + 3 < 7) {  // if has 3 more spaces to the right
                        if (testState.getAt(col, row) == player && testState.getAt(col + 1, row) == player && 
                            testState.getAt(col + 2, row) == player && testState.getAt(col + 3, row) == player) ourWins += 1;
 
                        else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row) == enemyPlayer && 
                            testState.getAt(col + 2, row) == enemyPlayer && testState.getAt(col + 3, row) == enemyPlayer) enemyWins += 1;

                        // int enemyH = 0;
                        // int ourH = 0;
                        // int zeroesH = 0;
                        // for (int d = 0; d < 4; d ++) {
                        //     if (testState.getAt(col + d, row) == player) ourH += 1;
                        //     else if (testState.getAt(col + d, row) == player) enemyH += 1;
                        //     else zeroesH += 1;
                        // }
                        // if (ourH == 3 && zeroesH == 1) our3Horz += 1;
                        // else if (enemyH == 3 && zeroesH == 1) enemy3Horz += 1;
 
 
                        if (row - 3 >= 0) {  // if has 3 more spaces above
                            if (testState.getAt(col, row) == player && testState.getAt(col + 1, row - 1) == player && 
                                testState.getAt(col + 2, row - 2) == player && testState.getAt(col + 3, row - 3) == player) ourWins += 1;
 
                            else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row - 1) == enemyPlayer && 
                                testState.getAt(col + 2, row - 2) == enemyPlayer && testState.getAt(col + 3, row - 3) == enemyPlayer) enemyWins += 1;
 
                        }  // Check diagonal RIGHT UP 4 in a row
 
                        if (row + 3 < 6) {  // if has 3 more spaces below
                            if (testState.getAt(col, row) == player && testState.getAt(col + 1, row + 1) == player && 
                                testState.getAt(col + 2, row + 2) == player && testState.getAt(col + 3, row + 3) == player) ourWins += 1;
 
                            else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row + 1) == enemyPlayer && 
                                testState.getAt(col + 2, row + enemyPlayer) == 2 && testState.getAt(col + 3, row + 3) == enemyPlayer) enemyWins += 1;
 
                        }  // Check diagonal RIGHT DOWN 4 in a row
 
                    }  // Check horizontal 4 in a row

                    if (col + 3 < 7) {  // if has 3 more spaces to the right
                        if (testState.getAt(col, row) == player && testState.getAt(col + 1, row) == player && 
                            testState.getAt(col + 2, row) == player && testState.getAt(col + 3, row) == player) ourWins += 1;
 
                        else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row) == enemyPlayer && 
                            testState.getAt(col + 2, row) == enemyPlayer && testState.getAt(col + 3, row) == enemyPlayer) enemyWins += 1;
 
 
                        if (row - 3 >= 0) {  // if has 3 more spaces above
                            if (testState.getAt(col, row) == player && testState.getAt(col + 1, row - 1) == player && 
                                testState.getAt(col + 2, row - 2) == player && testState.getAt(col + 3, row - 3) == player) ourWins += 1;
 
                            else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row - 1) == enemyPlayer && 
                                testState.getAt(col + 2, row - 2) == enemyPlayer && testState.getAt(col + 3, row - 3) == enemyPlayer) enemyWins += 1;
 
                            int enemyD = 0;
                            int ourD = 0;
                            int zeroes = 0;
                            for (int d = 0; d < 4; d ++) {
                                if (testState.getAt(col + d, row - d) == player) ourD += 1;
                                else if (testState.getAt(col + d, row - d) == player) enemyD += 1;
                                else zeroes += 1;
                            }
                            if (ourD == 3 && zeroes == 1) our3Diag += 1;
                            else if (enemyD == 3 && zeroes == 1) enemy3Diag += 1;
                        }  // Check diagonal RIGHT UP 4 in a row
 
                        if (row + 3 < 6) {  // if has 3 more spaces below
                            if (testState.getAt(col, row) == player && testState.getAt(col + 1, row + 1) == player && 
                                testState.getAt(col + 2, row + 2) == player && testState.getAt(col + 3, row + 3) == player) ourWins += 1;
 
                            else if (testState.getAt(col, row) == enemyPlayer && testState.getAt(col + 1, row + 1) == enemyPlayer && 
                                testState.getAt(col + 2, row + 2) == 2 && testState.getAt(col + 3, row + 3) == enemyPlayer) enemyWins += 1;
 
                            int enemyD = 0;
                            int ourD = 0;
                            int zeroes = 0;
                            for (int d = 0; d < 4; d ++) {
                                if (testState.getAt(col + d, row + d) == player) ourD += 1;
                                else if (testState.getAt(col + d, row + d) == player) enemyD += 1;
                                else zeroes += 1;
                            }
                            if (ourD == 3 && zeroes == 1) our3Diag += 1;
                            else if (enemyD == 3 && zeroes == 1) enemy3Diag += 1;
                        }  // Check diagonal RIGHT DOWN 4 in a row

 
                    }  // Check horizontal 4 in a row
 
                    //System.out.println("   " + ourWeight + ",  " + enemyWeight);
                }  // Start from top
                //System.out.println();
 
            }  // Start from left

        //System.out.println(enemyWins);
             
        return ((ourWins - enemyWins) * (winWeight)) + 
               ((ourTwoInRow - enemyTwoInRow) * (twoHorzWeight)) +
               //((our3Horz - enemy3Horz) * threeHorzWeight) + 
               ((our3Diag - enemy3Diag) * threeDiagWeight) +
               (1 * (ourWeight - enemyWeight));
    }
}