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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix == null)
            return words.get(random.nextInt(words.size()));

        int search = binarySearch(prefix, words);

        if(search== -1)
            return null;
        return words.get(search);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix == null)
            return words.get(random.nextInt(words.size()));

        int search = binarySearch(prefix, words);
        if(search ==-1)
            return null;

        String word = words.get(search);
        int index = search;
        if(GhostActivity.turn == 0){
            ArrayList<String> evenWords = new ArrayList<>();
            while (word.startsWith(prefix)){
                if(word.length()%2 == 0)
                    evenWords.add(word);
                if (index == 0 )
                    break;
                word = words.get(--index);
            }
            index = search;
            word = words.get(index);
            while (word.startsWith(prefix)){
                if(word.length()%2 == 0)
                    evenWords.add(word);
                if(index == words.size()-1)
                    break;
                word = words.get(++index);
            }
            return evenWords.get(random.nextInt(evenWords.size()));
        }
        else{

            ArrayList<String> oddWords = new ArrayList<>();
            while (word.startsWith(prefix)){
                if(word.length()%2 == 1)
                    oddWords.add(word);
                if (index == 0 )
                    break;
                word = words.get(--index);
            }
            index = search;
            word = words.get(index);
            while (word.startsWith(prefix)){
                if(word.length()%2 == 1)
                    oddWords.add(word);
                if(index == words.size()-1)
                    break;
                word = words.get(++index);
            }
            return oddWords.get(random.nextInt(oddWords.size()));
        }
    }


    public int binarySearch(String key, ArrayList<String> wordList){
        int startIndex = 0;
        int endIndex = wordList.size()-1;
        int midIndex;
        int compare;
        while(startIndex <= endIndex) {
            midIndex  = (endIndex+startIndex) >>> 1;
            compare = wordList.get(midIndex).compareTo(key);
            if (wordList.get(midIndex).startsWith(key))
                return midIndex;
            else if ( compare < 0) {
                startIndex = midIndex + 1;
            } else if (compare > 0) {
                endIndex = midIndex - 1;
            }
        }
        return -1;

    }
}
