#include <iostream>
#include "coin.h"

using namespace std;

int main() {
    size_t count, *coins, sum;
    cout << "\nPlease input count of coins: ";
    cin >> count;
    coins = new size_t[count];
    cout << "\nPlease input the denomination of the coins, separate by space or CR: ";
    for (size_t i = 0; i < count; i++) {
        cin >> coins[i];
    }

    cout << "\nPlease input the total mount of cents you want: ";
    cin >> sum;

    cout << endl << sum << " needs ";
//    size_t coins[] = {1, 3, 4, 7, 9, 11};
    CoinProblem p(sum, coins, count);
    Solution* x = p.getSolution();
    for (size_t i = 0; i < count; i++) {
        cout << x->getCoinCount(i) << "x" << coins[i] << "\t";
    }
    cout << endl;
}
