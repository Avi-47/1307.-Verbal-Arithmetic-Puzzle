class Solution {
    Map<Character, Integer> charToDigit = new HashMap<>();
    boolean[] used = new boolean[10];
    List<String> words;
    String result;
    int maxLen;
    public boolean isSolvable(String[] words, String result) {
        this.words = Arrays.asList(words);
        this.result = result;
        int k = 0;
        for(String str:words) k = Math.max(str.length(),k);
        k = Math.max(result.length(),k);
        this.maxLen = k;

        Set<Character> leading = new HashSet<>();
        for (String w : words) if (w.length() > 1) leading.add(w.charAt(0));
        if (result.length() > 1) leading.add(result.charAt(0));

        return dfs(0, 0, leading);
    }
    private boolean dfs(int pos, int carry, Set<Character> leading) {
        if (pos == maxLen) {
            return carry == 0;
        }
        int sum = carry;
        for (String w : words) {
            int idx = w.length() - 1 - pos;
            if (idx >= 0) {
                char ch = w.charAt(idx);
                if (charToDigit.containsKey(ch)) {
                    sum += charToDigit.get(ch);
                } else {
                    for (int d = 0; d <= 9; d++) {
                        if (used[d]) continue;
                        if (d == 0 && leading.contains(ch)) continue;
                        charToDigit.put(ch, d);
                        used[d] = true;
                        if (dfs(pos, carry, leading)) return true;
                        used[d] = false;
                        charToDigit.remove(ch);
                    }
                    return false;
                }
            }
        }
        int idxR = result.length() - 1 - pos;
        if (idxR < 0) {
            return false;
        }
        char rch = result.charAt(idxR);
        int expected = sum % 10;
        int nextCarry = sum / 10;

        if (charToDigit.containsKey(rch)) {
            if (charToDigit.get(rch) != expected) return false;
            return dfs(pos + 1, nextCarry, leading);
        } else {
            if (used[expected]) return false;
            if (expected == 0 && leading.contains(rch)) return false;
            charToDigit.put(rch, expected);
            used[expected] = true;
            if (dfs(pos + 1, nextCarry, leading)) return true;
            used[expected] = false;
            charToDigit.remove(rch);
        }
        return false;
    }
}
