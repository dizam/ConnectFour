// Brian Becker, 999646986
// Daljodh Pannu, 912303549
 
public class alphabeta_a_dot_out extends AIModule {
 
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
        } //guarantees the default move is a valid move


        int finalDepth = 4;

        us = state.getActivePlayer();
        if (us == 1) them = 2;
        if (first == 1)
        {
            chosenMove = 3;
            first++;
        } //the best opening move is always the center
        else
        {
            while(!terminate) 
            {
                 minimax_decision(state.copy(), 0, finalDepth);
 
                 if(!terminate) chosenMove = BestMove;
 
             finalDepth += 1;
            } //iterative depth, look farther and farther down the tree until told to terminate by 500ms
        } //not opening move
         
    } //function to choose the next move
 
    // alphabeta_decision funciton as described in book
    private int minimax_decision(GameStateModule curState, int curDepth, int finalDepth) {
         
        return max_value(curState, curDepth, finalDepth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
 
    private int min_value(GameStateModule testState, int curDepth, int finalDepth, int alpha, int beta) {
 
        if (terminate) return 0;
        if (curDepth == finalDepth) return utility(testState, them);
            // Have reached max iteration depth, return estimated value
 
        curDepth += 1;
       
        for(int i : normalBestOrder) {
            if (testState.canMakeMove(i)) {
 
                GameStateModule temp = testState.copy();
                temp.makeMove(i);

                beta = Math.min(max_value(temp, curDepth, finalDepth, alpha, beta), beta);
                if (alpha >= beta) return beta;

            }  // move is available
        }
         
        return beta;
    }
 
    private int max_value(GameStateModule testState, int curDepth, int finalDepth, int alpha, int beta) {
         
        if (terminate) return 0;
        if (curDepth == finalDepth) return utility(testState, us);
            // Have reached max iteration depth, return estimated value
 
        curDepth += 1;
 
        for(int i : normalBestOrder) {
            if (testState.canMakeMove(i)) {

                GameStateModule temp = testState.copy();
                temp.makeMove(i);

                // We can work around the book's psuedocode 'v' value by just keeping it as alpha

                int prev = alpha;  // store previous value of alpha to update our best move
                alpha = Math.max(alpha, min_value(temp, curDepth, finalDepth, alpha, beta));
                if (curDepth == 1 && prev != alpha) BestMove = i;
                    // at depth of 1 and we got a better move, so update BestMove

                if (alpha >= beta) return alpha; 
 
            } // move is available
        }

        return alpha;
    }
 
    //our evaluation function based on open ended two in a row, three in a row horizontally,
    //three in a row diagonally, and four in a row (win condition).
    private int utility(GameStateModule testState, int player) {  // Sorry this is super ugly
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
                        // } //three in a row horizontal
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
                            } //three in a row diagonal up right
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
                            } //three in a row diagonal down right
                            if (ourD == 3 && zeroes == 1) our3Diag += 1;
                            else if (enemyD == 3 && zeroes == 1) enemy3Diag += 1;
                        }  // Check diagonal RIGHT DOWN 4 in a row

                    }  // Check horizontal 4 in a row
 
                }  // Start from top
 
            }  // Start from left
             
        return ((ourWins - enemyWins) * (winWeight)) + 
               ((ourTwoInRow - enemyTwoInRow) * (twoHorzWeight)) +
               //((our3Horz - enemy3Horz) * threeHorzWeight) + 
               ((our3Diag - enemy3Diag) * threeDiagWeight) +
               (1 * (ourWeight - enemyWeight));
    }

}  // Utility function as per book psuedocode