#ifndef __SHE_COIN_PROBLEM_H__
    #define __SHE_COIN_PROBLEM_H__


class Solution {
  private:
    static size_t coin_type_count;
    size_t* coins;
    size_t coin_total_count;
  public:
    Solution(const size_t c[]);
    Solution(Solution* s);
    ~Solution();
    void increaseCoinCount(unsigned index) { coins[index]++; coin_total_count++; }
    size_t getCoinCount(unsigned index) { return coins[index]; }
    size_t getCoinTotalCount() { return coin_total_count; }
    static void setCoinTypeCount(size_t c) { coin_type_count = c; }
};


class CoinProblem {
  private:
    static size_t coin_type_count;
    size_t sum;
    size_t* coins;
    Solution** s;
    void solve(size_t sum);
  public:
    CoinProblem(size_t sm, size_t c[], size_t coin_type_c);
    ~CoinProblem();
    Solution* getSolution();
};

#endif
