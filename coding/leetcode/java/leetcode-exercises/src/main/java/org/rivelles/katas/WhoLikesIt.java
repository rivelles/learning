package org.rivelles.katas;
/*
You probably know the "like" system from Facebook and other pages. People can "like" blog posts, pictures or other items. We want to create the text that should be displayed next to such an item.

Implement the function which takes an array containing the names of people that like an item. It must return the display text as shown in the examples:

[]                                -->  "no one likes this"
["Peter"]                         -->  "Peter likes this"
["Jacob", "Alex"]                 -->  "Jacob and Alex like this"
["Max", "John", "Mark"]           -->  "Max, John and Mark like this"
["Alex", "Jacob", "Mark", "Max"]  -->  "Alex, Jacob and 2 others like this"
Note: For 4 or more names, the number in "and 2 others" simply increases.
 */
public class WhoLikesIt {
    public static String whoLikesIt(String... names) {
        var userCount = names.length;

        var actors = "";
        var verb = "like"+ (userCount <= 1 ? "s" : "");
        if (userCount == 0) actors = "no one";
        else if (userCount == 1) actors = names[0];
        else if (userCount == 2) actors = "%s and %s".formatted(names[0], names[1]);
        else if (userCount == 3) actors = "%s, %s and %s".formatted(names[0], names[1], names[2]);
        else actors = "%s, %s and %d others".formatted(names[0], names[1], names.length-2);

        return actors+" "+verb+" this";
    }
}
