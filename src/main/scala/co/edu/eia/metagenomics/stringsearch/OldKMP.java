package co.edu.eia.metagenomics.stringsearch;

public class OldKMP {
    private static int[] bestPartialMatch(String pattern) {
        int[] f = null;
        if (pattern != null && !pattern.equals("")) {
            f = new int[pattern.length()+1];
            f[0] = f[1] = 0;
            for (int i=2; i<=pattern.length(); i++) {
                int j = f[i-1];
                while (true) {
                    if (pattern.charAt(j)==pattern.charAt(i-1)){
                        f[i]=j+1; break;
                    }
                    if (j==0){
                        f[i]=0; break;
                    }
                    j=f[j];
                }
            }
        }
        return f;
    }

    public void search(String text, String pattern) {
        int[] f = bestPartialMatch(pattern);
        int i = 0;
        int j = 0;
        while (i<text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == (pattern.length())) {
                    System.out.println("Pattern:"+ (i-j) +"-"+(i-1));
                    j=f[j];
                }
            }
            else if (j > 0) j = f[j];
            else i++;
        }
    }
}
