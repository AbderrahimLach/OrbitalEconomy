package xyz.directplan.orbitaleconomy.lib.config.replacement.characters;


import xyz.directplan.orbitaleconomy.lib.config.replacement.ReplacementChar;

/**
 * @author DirectPlan
 */
public class BracketReplacementChar implements ReplacementChar {

    @Override
    public char start() {
        return '{';
    }

    @Override
    public char end() {
        return '}';
    }
}
