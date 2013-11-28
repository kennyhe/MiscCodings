#include <stdio.h>

#define MAX 100
#define L(i) x[i]
#define R(i) x[i+1]

int x[MAX + 1];
int d[MAX][MAX];


int main() {
    int i, k, dist, v, min, n;
    printf("\ninput number: ");
    scanf("%d", &n);
    printf("\nplease input the %d dimensions: ", n+1);
    for (i = 0; i <= n; i++) scanf("%d", x+i);
    
    for (i = 0; i < n -1; i++) { 
        d[i][i] = 0;
        d[i][i+1] = L(i) * R(i) * R(i+1);
    }
    d[n-1][n-1] = 0;


    for (dist = 2; dist <= n - 1; dist++) {
        for (i = 0; i + dist <= n-1; i++) {
            // Get the value of d[i][i+dist]
            min = 0xfffff;
            for (k = 0; k < dist; k++) {
                v = d[i][i+k] + L(i) * R(i+k) * R(i+dist) + d[i+k+1][i+dist];
                if (v < min)
                    min = v;
            }
            d[i][i+dist] = min;
        }
    }
    
    printf("\nMin multiply count: %d\n", d[0][n -1]);


    // Debug: output the progress.
    for (i = 0; i <= n - 1; i++) {
        for (k = 0; k <= n - 1; k++) {
            if (k > i) {
                printf("%10d", d[i][k]);
            } else {
                printf("%10d", 0);
            }
        }
        printf("\n");
    }

}

