#include <iostream>
#include "coin.h"

using namespace std;

size_t Solution::coin_type_count = 0;

Solution::Solution(const size_t c[]) {
    coins = new size_t[coin_type_count];
    coin_total_count = 0;
    for (size_t i = 0; i < coin_type_count; i++) {
        coins[i] = c[i];
        coin_total_count += c[i];
    }
}

Solution::Solution(Solution* s) {
    coins = new size_t[coin_type_count];
    for (size_t i = 0; i < coin_type_count; i++) {
        coins[i] = s->getCoinCount(i);
    }
    coin_total_count = s->getCoinTotalCount();
}

Solution::~Solution() {
    delete[] coins;
}



size_t CoinProblem::coin_type_count = 0;

CoinProblem::CoinProblem(size_t sm, size_t c[], size_t ctc) {
    if (c[0] != 1) {
       cout << "Warning: The smallest coin type is not 1, which may cause this problem has no solution!" << endl;
    }
    coin_type_count = ctc;
    Solution::setCoinTypeCount(ctc);
    sum = sm;
    coins = c;
    s = new Solution*[sum + 1];
    for (size_t i = 0; i <= sum; i++) {
        s[i] = NULL;
    }

    solve(sum);
}

CoinProblem::~CoinProblem() {
    for (size_t i = 0; i <= sum; i++) {
        if (s[i]) {
            delete s[i];
        }
    }
    delete []s;
}

Solution* CoinProblem::getSolution() {
    return s[sum];
}

void CoinProblem::solve(size_t sum) {
    if (sum < coins[0]) { // No solution, trim the gap and get the solution.
        size_t* empty = new size_t[coin_type_count];
        for (size_t i = 0; i < coin_type_count; i++) {
            empty[i] = 0;
        }
        s[0] = new Solution(empty);
        delete []empty;
        return;
    }

    for (size_t i = 0; i < coin_type_count; i++) {
        if (coins[i] == sum) {
            if (s[0] == NULL)
                solve(0);

            s[coins[i]] = new Solution(s[0]);
            s[coins[i]]->increaseCoinCount(i);
            return;
        }
    }

    size_t min_index= sum - coins[0], index, min_i = 0;
    if (s[min_index] == NULL) {
         solve(min_index);
    }

    for (size_t i = 1; i < coin_type_count; i++) {
        if (sum > coins[i]) {
            index = sum - coins[i];
    	    if (s[index] == NULL) {
                solve(index);
            }
            if (s[min_index]->getCoinTotalCount() > s[index]->getCoinTotalCount()) {
                min_index = index;
                min_i = i;
            }
        }
    }

    s[sum] = new Solution(s[min_index]);
    s[sum]->increaseCoinCount(min_i);
    return;
}

