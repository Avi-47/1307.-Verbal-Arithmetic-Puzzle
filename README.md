<summary><b>1307. Verbal Arithmetic Puzzle 🔢</b></summary>

### 📘 Problem Statement
You are given an equation, represented by **words** on the left side and the **result** on the right side.  

You need to check if the equation is **solvable** under the following rules:

- Each character is mapped to a digit (0–9).  
- No two characters map to the same digit.  
- Each `words[i]` and `result` must not have **leading zeros**.  
- The sum of the numbers represented by `words` equals the number represented by `result`.  

Return **true** if the equation is solvable, otherwise return **false**.

---

### 🧪 Examples

**Example 1:**

Input: words = ["SEND","MORE"], result = "MONEY"

Output: true

Explanation:
Map 'S'->9, 'E'->5, 'N'->6, 'D'->7, 'M'->1, 'O'->0, 'R'->8, 'Y'->2
9567 + 1085 = 10652

**Example 2:**

Input: words = ["SIX","SEVEN","SEVEN"], result = "TWENTY"

Output: true

Explanation:
Map 'S'->6, 'I'->5, 'X'->0, 'E'->8, 'V'->7, 'N'->2, 'T'->1, 'W'->3, 'Y'->4
650 + 68782 + 68782 = 138214

**Example 3:**

Input: words = ["LEET","CODE"], result = "POINT"

Output: false

Explanation: No valid digit assignment exists.


---

### 💡 Solution Idea
This is a **backtracking + DFS** problem:
1. Track character-to-digit assignments in a map.
2. Ensure no digit is reused (using a `used` array).
3. Handle **leading zero constraint** separately.
4. Perform DFS column by column (from least significant digit to most), propagating carries.
5. Return true if all columns satisfy the equation.

---

### ✅ Java Code
```java
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
        for (String str : words) k = Math.max(str.length(), k);
        k = Math.max(result.length(), k);
        this.maxLen = k;

        // Track leading characters (cannot be zero)
        Set<Character> leading = new HashSet<>();
        for (String w : words) if (w.length() > 1) leading.add(w.charAt(0));
        if (result.length() > 1) leading.add(result.charAt(0));

        return dfs(0, 0, leading);
    }

    private boolean dfs(int pos, int carry, Set<Character> leading) {
        if (pos == maxLen) return carry == 0;

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
        if (idxR < 0) return false;

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
```
⏱ Complexity Analysis

Time Complexity: O(10^C) worst-case, where C = number of distinct characters (≤ 10).

Space Complexity: O(C + 10) for recursion, maps, and digit usage tracking.
