package com.lewish.start.htmlspanner.handlers;

import android.text.SpannableStringBuilder;
import android.util.Log;

import com.lewish.start.htmlspanner.SpanStack;
import com.lewish.start.htmlspanner.TagNodeHandler;
import com.lewish.start.htmlspanner.css.CSSCompiler;
import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;

/**
 * TagNodeHandler that reads <style> blocks and parses the CSS rules within.
 */
public class StyleNodeHandler extends TagNodeHandler {

    @Override
    public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end, SpanStack spanStack) {

        if ( getSpanner().isAllowStyling() ) {

            if ( node.getAllChildren().size() == 1 ) {
                Object childNode = node.getAllChildren().get(0);

                if ( childNode instanceof ContentNode ) {
                    parseCSSFromText( ( (ContentNode) childNode ).getContent(),
                            spanStack );
                }
            }
        }

    }

    private void parseCSSFromText(String text, SpanStack spanStack ) {
        try {
            for ( Rule rule: CSSParser.parse( text ) ) {
                spanStack.registerCompiledRule(CSSCompiler.compile(rule, getSpanner()));
            }
        } catch ( Exception e ) {
            Log.e( "StyleNodeHandler", "Unparseable CSS definition", e );
        }
    }

    @Override
    public boolean rendersContent() {
        return true;
    }
}
