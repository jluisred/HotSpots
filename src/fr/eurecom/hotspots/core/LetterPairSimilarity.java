/*    */ package fr.eurecom.hotspots.core;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
/*    */ 
/*    */ public class LetterPairSimilarity
/*    */ {
/*    */   public static double compareStrings(String str1, String str2)
/*    */   {
/* 14 */     if ((str1.length() <= 0) || (str2.length() <= 0)) {
/* 15 */       System.out.println("EMPTY LABEL: " + str1 + " , " + str2);
/* 16 */       return 0.0D;
/*    */     }
/*    */ 

			//Remove punctuations:
			str1 = str1.replaceAll("\\.", "");
			str2 = str2.replaceAll("\\.", "");
			str1 = str1.replaceAll("-", " ");
			str2 = str2.replaceAll("-", " ");


			String[] SplittedSRT1 = str1.split(" ");
			String[] SplittedSRT2 = str2.split(" ");
			
			Vector <String> v1 = new Vector <String> ();
			Vector <String> v2 = new Vector <String> ();

			v1.addAll(Arrays.asList(SplittedSRT1));
			v2.addAll(Arrays.asList(SplittedSRT2));

			if (v1.contains(str2) || v2.contains(str1)) {
				return 1.0;
			}
			
			

/* 19 */     ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
/* 20 */     ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
/* 21 */     int intersection = 0;
/* 22 */     int union = pairs1.size() + pairs2.size();
/* 23 */     for (int i = 0; i < pairs1.size(); i++) {
/* 24 */       Object pair1 = pairs1.get(i);
/* 25 */       for (int j = 0; j < pairs2.size(); j++) {
/* 26 */         Object pair2 = pairs2.get(j);
/* 27 */         if (pair1.equals(pair2)) {
/* 28 */           intersection++;
/* 29 */           pairs2.remove(j);
/* 30 */           break;
/*    */         }
/*    */       }
/*    */     }
/* 34 */     return 2.0D * intersection / union;
/*    */   }
/*    */ 
/*    */   private static String[] letterPairs(String str)
/*    */   {
/* 40 */     int numPairs = str.length() - 1;
/* 41 */     String[] pairs = new String[numPairs];
/* 42 */     for (int i = 0; i < numPairs; i++) {
/* 43 */       pairs[i] = str.substring(i, i + 2);
/*    */     }
/* 45 */     return pairs;
/*    */   }
/*    */ 
/*    */   private static ArrayList<String> wordLetterPairs(String str)
/*    */   {
/* 52 */     ArrayList allPairs = new ArrayList();
/*    */ 
/* 54 */     String[] words = str.split("\\s");
/*    */ 
/* 56 */     for (int w = 0; w < words.length; w++)
/*    */     {
/* 58 */       if (words[w].length() > 0) {
/* 59 */         String[] pairsInWord = letterPairs(words[w]);
/* 60 */         for (int p = 0; p < pairsInWord.length; p++) {
/* 61 */           allPairs.add(pairsInWord[p]);
/*    */         }
/*    */       }
/*    */     }
/* 65 */     return allPairs;
/*    */   }
/*    */ }

/* Location:           /Users/redondo/Documents/semantic_SVN/linkedtv/projects/metadata-viewer/application/EntityContext/entityContext.jar
 * Qualified Name:     fr.eurecom.entityanalyzer.core.LetterPairSimilarity
 * JD-Core Version:    0.6.2
 */