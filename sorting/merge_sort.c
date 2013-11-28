#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void merge_sort(int x[], int count) {
    if (count == 1) {
        return;
    } else if (count == 2) {
        if (x[0] > x[1]) { // Exchange X[0] and X[1]
            int tmp = x[0];
            x[0] = x[1];
            x[1] = tmp;
        }
        return;
    } else { // count > 2
        int mid = count / 2;
        merge_sort(x, mid); // Apply this function on the left half part
        merge_sort(x + mid, count - mid); // Apply on the right half part
        int s1 = 0, s2 = mid, pos = 0;
        int* result = (int*) malloc(sizeof(int) * count);
        while ((s1 < mid) && (s2 < count)) {
            if (x[s1] <= x[s2]) {
                result[pos++]  = x[s1++];
            } else {
                result[pos++]  = x[s2++];
            }
        }
        while (s1 < mid) { // move the rest of left part
            result[pos++]  = x[s1++];
        }
        while (s2 < count) { // move the rest of right part
            result[pos++]  = x[s2++];
        }
        memcpy(x, result, sizeof(int) * count);
    }
}

void quick_sort(int x[], int left, int right) {
    if (left >= right) 
        return;

    int tmp;
    if (left + 1 == right) {
        if (x[left] > x[right]) {
            tmp = x[left]; x[left] = x[right]; x[right] = tmp; // swap (x[left], x[right]);
        }
        return;
    }

    // pick out a number
    int mid = x[right];
    int i = left, j = right - 1;
    while (i < j) {
        while ((i <= j) && (x[i] < mid)) i++;
        while ((i <= j) && (x[j] > mid)) j--;
        printf("i = %d, j = %d, pivot = %d\n", i, j, mid);
        if (i < j) {
            tmp = x[i]; x[i] = x[j]; x[j] = tmp; // swap (x[i], x[j]);
        }
        for (tmp = left; tmp <= right; tmp++) printf("\t%d", x[tmp]); printf("\n");
    }
    // if (i == j) // x[i] == x[j] == x[right]
    if (x[i] > mid) {
        // Shift x[i] and x[right]
        tmp = x[i]; x[i] = mid; x[right] = tmp;
        for (tmp = left; tmp <= right; tmp++) printf("\t%d", x[tmp]); printf("\n");
    }
    quick_sort(x, left, i - 1);
    quick_sort(x, i + 1, right);
}

int main(void) {
    int array[] = {3,12,5,2,7,4,6,8,1,10,18,11,13,15,16,14,17,9};
    int count = 18;
    printf("The unsorted array: ");
    int i;
    for (i = 0; i < count; i++) printf("%d ", array[i]);
    printf("\n");
    // merge_sort(array, count);
    quick_sort(array, 0, 17);
    printf("The sorted array: ");
    for (i = 0; i < count; i++) printf("%d ", array[i]);
    printf("\n");
}    
