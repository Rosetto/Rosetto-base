package info.rosetto.parsers.rosetto;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.parsers.AbstractTagParser;

public class RosettoTagParser extends AbstractTagParser {

    @Override
    public ActionCall convertTag(String tagName, String args) {
        return new ActionCall(tagName, args);
    }

}
