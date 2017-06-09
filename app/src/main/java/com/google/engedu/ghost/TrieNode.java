/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private Random random = new Random();

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        char[] word = s.toCharArray();
        TrieNode value = null;
        HashMap<Character, TrieNode> tmp = this.children;
        for (char i : word) {
            value = tmp.get(i);
            if (value != null) {
                tmp = value.children;
            } else {
                TrieNode child = new TrieNode();
                tmp.put(i, child);
                tmp = child.children;
                value = child;
            }
        }
        value.isWord = true;
    }

    public boolean isWord(String s) {
        char[] word = s.toCharArray();
        TrieNode value = null;
        HashMap<Character, TrieNode> tmp = this.children;
        for (char i : word) {
            value = tmp.get(i);
            if (value != null) {
                tmp = value.children;
            } else
                return false;
        }
        if (value.isWord == true)
            return true;
        return false;
    }

    public String getAnyWordStartingWith(String s) {
        char[] word = s.toCharArray();
        TrieNode value;
        HashMap<Character, TrieNode> tmp = this.children;
        for (char i : word) {
            value = tmp.get(i);
            if (value != null) {
                tmp = value.children;
            } else
                return null;
        }
        if (tmp != null) {
            String Word = s;
            while (true) {
                List<Character> keysAsArray = new ArrayList<Character>(tmp.keySet());
                char c = keysAsArray.get(random.nextInt(keysAsArray.size()));
                Word = Word + c;
                value = tmp.get(c);
                if (value.isWord == true)
                    return Word;
                tmp = value.children;
            }
        }
        return null;
    }

    public String getGoodWordStartingWith(String s) {

        char[] word = s.toCharArray();
        TrieNode value;
        HashMap<Character, TrieNode> tmp = this.children;
        for (char i : word) {
            value = tmp.get(i);
            if (value != null)
                tmp = value.children;
            else
                return null;
        }
        if (tmp != null) {

            String Word = s;
            char ch;
            List<Character> keysArray = new ArrayList<Character>(tmp.keySet());
            List<Character> keysNotWord = new ArrayList<Character>();
            for(char c:keysArray) {
                value = tmp.get(c);
                if(value.isWord == false)
                    keysNotWord.add(c);
            }
            if(keysNotWord.size()==0)
                return Word+keysArray.get(random.nextInt(keysArray.size()));
            else {
                ch = keysNotWord.get(random.nextInt(keysNotWord.size()));
                Word+=ch;
                value = tmp.get(ch);
                tmp = value.children;
            }
            while (true) {
                List<Character> keysAsArray = new ArrayList<Character>(tmp.keySet());
                char c = keysAsArray.get(random.nextInt(keysAsArray.size()));
                Word = Word + c;
                value = tmp.get(c);
                if (value.isWord == true)
                    return Word;
                tmp = value.children;
                }
            }
        return null;
    }
}