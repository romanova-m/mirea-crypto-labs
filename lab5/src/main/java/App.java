public class App {

    public static void main(String[] args) {
        int[] array = {6, 2, 1, 2, 8, 10};
        sort(array, 1, 10);
    }

    private static void sort(int[] array, int minVal, int maxVal) {
        int[] temp = new int[maxVal - minVal + 1];
        for (int elem : array) {
            temp[elem - minVal]++;
        }
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i]; j++) {
                System.out.print(i + minVal + " ");
            }
        }
    }
}
