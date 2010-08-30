// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 nofs\\restfs\\json\\json.g 2010-08-24 21:25:30

package nofs.restfs.json;

import java.util.regex.Pattern;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class jsonParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "STRING", "NUMBER", "OBJECT", "FIELD", "ARRAY", "COMMA", "TRUE", "FALSE", "NULL", "String", "Number", "Exponent", "Digit", "EscapeSequence", "WS", "UnicodeEscape", "HexDigit", "'true'", "'false'", "'null'", "'{'", "'}'", "'['", "']'", "':'"
    };
    public static final int T__28=28;
    public static final int T__27=27;
    public static final int T__26=26;
    public static final int T__25=25;
    public static final int T__24=24;
    public static final int T__23=23;
    public static final int T__22=22;
    public static final int T__21=21;
    public static final int NULL=12;
    public static final int NUMBER=5;
    public static final int Exponent=15;
    public static final int Digit=16;
    public static final int EOF=-1;
    public static final int HexDigit=20;
    public static final int TRUE=10;
    public static final int WS=18;
    public static final int Number=14;
    public static final int OBJECT=6;
    public static final int COMMA=9;
    public static final int UnicodeEscape=19;
    public static final int FIELD=7;
    public static final int String=13;
    public static final int FALSE=11;
    public static final int EscapeSequence=17;
    public static final int ARRAY=8;
    public static final int STRING=4;

    // delegates
    // delegators


        public jsonParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public jsonParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return jsonParser.tokenNames; }
    public String getGrammarFileName() { return "nofs\\restfs\\json\\json.g"; }


    protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException
    {
    	throw new MismatchedTokenException(ttype, input);
    }
    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException
    {
    	throw e;
    }

    public java.util.List<RecognitionException> Errors = new java.util.LinkedList<RecognitionException>();

    public void displayRecognitionError(String[] tokenNames,
                                            RecognitionException e) {
            String hdr = getErrorHeader(e);
    	String msg = getErrorMessage(e, tokenNames);
    	Errors.add(e);
    }



    public static class value_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // nofs\\restfs\\json\\json.g:54:1: value : ( string | number | object | array | 'true' -> TRUE | 'false' -> FALSE | 'null' -> NULL );
    public final jsonParser.value_return value() throws RecognitionException {
        jsonParser.value_return retval = new jsonParser.value_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token string_literal5=null;
        Token string_literal6=null;
        Token string_literal7=null;
        jsonParser.string_return string1 = null;

        jsonParser.number_return number2 = null;

        jsonParser.object_return object3 = null;

        jsonParser.array_return array4 = null;


        CommonTree string_literal5_tree=null;
        CommonTree string_literal6_tree=null;
        CommonTree string_literal7_tree=null;
        RewriteRuleTokenStream stream_21=new RewriteRuleTokenStream(adaptor,"token 21");
        RewriteRuleTokenStream stream_22=new RewriteRuleTokenStream(adaptor,"token 22");
        RewriteRuleTokenStream stream_23=new RewriteRuleTokenStream(adaptor,"token 23");

        try {
            // nofs\\restfs\\json\\json.g:56:2: ( string | number | object | array | 'true' -> TRUE | 'false' -> FALSE | 'null' -> NULL )
            int alt1=7;
            switch ( input.LA(1) ) {
            case String:
                {
                alt1=1;
                }
                break;
            case Number:
                {
                alt1=2;
                }
                break;
            case 24:
                {
                alt1=3;
                }
                break;
            case 26:
                {
                alt1=4;
                }
                break;
            case 21:
                {
                alt1=5;
                }
                break;
            case 22:
                {
                alt1=6;
                }
                break;
            case 23:
                {
                alt1=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // nofs\\restfs\\json\\json.g:56:4: string
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_string_in_value107);
                    string1=string();

                    state._fsp--;

                    adaptor.addChild(root_0, string1.getTree());

                    }
                    break;
                case 2 :
                    // nofs\\restfs\\json\\json.g:57:4: number
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_number_in_value112);
                    number2=number();

                    state._fsp--;

                    adaptor.addChild(root_0, number2.getTree());

                    }
                    break;
                case 3 :
                    // nofs\\restfs\\json\\json.g:58:4: object
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_object_in_value117);
                    object3=object();

                    state._fsp--;

                    adaptor.addChild(root_0, object3.getTree());

                    }
                    break;
                case 4 :
                    // nofs\\restfs\\json\\json.g:59:4: array
                    {
                    root_0 = (CommonTree)adaptor.nil();

                    pushFollow(FOLLOW_array_in_value122);
                    array4=array();

                    state._fsp--;

                    adaptor.addChild(root_0, array4.getTree());

                    }
                    break;
                case 5 :
                    // nofs\\restfs\\json\\json.g:60:4: 'true'
                    {
                    string_literal5=(Token)match(input,21,FOLLOW_21_in_value127);  
                    stream_21.add(string_literal5);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 60:11: -> TRUE
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(TRUE, "TRUE"));

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 6 :
                    // nofs\\restfs\\json\\json.g:61:4: 'false'
                    {
                    string_literal6=(Token)match(input,22,FOLLOW_22_in_value136);  
                    stream_22.add(string_literal6);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 61:12: -> FALSE
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(FALSE, "FALSE"));

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 7 :
                    // nofs\\restfs\\json\\json.g:62:4: 'null'
                    {
                    string_literal7=(Token)match(input,23,FOLLOW_23_in_value145);  
                    stream_23.add(string_literal7);



                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 62:11: -> NULL
                    {
                        adaptor.addChild(root_0, (CommonTree)adaptor.create(NULL, "NULL"));

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class string_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string"
    // nofs\\restfs\\json\\json.g:65:1: string : String -> ^( STRING String ) ;
    public final jsonParser.string_return string() throws RecognitionException {
        jsonParser.string_return retval = new jsonParser.string_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token String8=null;

        CommonTree String8_tree=null;
        RewriteRuleTokenStream stream_String=new RewriteRuleTokenStream(adaptor,"token String");

        try {
            // nofs\\restfs\\json\\json.g:65:9: ( String -> ^( STRING String ) )
            // nofs\\restfs\\json\\json.g:65:11: String
            {
            String8=(Token)match(input,String,FOLLOW_String_in_string160);  
            stream_String.add(String8);



            // AST REWRITE
            // elements: String
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 66:4: -> ^( STRING String )
            {
                // nofs\\restfs\\json\\json.g:66:7: ^( STRING String )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(STRING, "STRING"), root_1);

                adaptor.addChild(root_1, stream_String.nextNode());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string"

    public static class number_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "number"
    // nofs\\restfs\\json\\json.g:74:1: number : n= Number {...}? ( Exponent )? -> ^( NUMBER Number ( Exponent )? ) ;
    public final jsonParser.number_return number() throws RecognitionException {
        jsonParser.number_return retval = new jsonParser.number_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token n=null;
        Token Exponent9=null;

        CommonTree n_tree=null;
        CommonTree Exponent9_tree=null;
        RewriteRuleTokenStream stream_Exponent=new RewriteRuleTokenStream(adaptor,"token Exponent");
        RewriteRuleTokenStream stream_Number=new RewriteRuleTokenStream(adaptor,"token Number");

        try {
            // nofs\\restfs\\json\\json.g:74:8: (n= Number {...}? ( Exponent )? -> ^( NUMBER Number ( Exponent )? ) )
            // nofs\\restfs\\json\\json.g:74:10: n= Number {...}? ( Exponent )?
            {
            n=(Token)match(input,Number,FOLLOW_Number_in_number188);  
            stream_Number.add(n);

            if ( !((Pattern.matches("(0|(-?[1-9]\\d*))(\\.\\d+)?", n.getText()))) ) {
                throw new FailedPredicateException(input, "number", "Pattern.matches(\"(0|(-?[1-9]\\\\d*))(\\\\.\\\\d+)?\", n.getText())");
            }
            // nofs\\restfs\\json\\json.g:75:6: ( Exponent )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==Exponent) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // nofs\\restfs\\json\\json.g:75:6: Exponent
                    {
                    Exponent9=(Token)match(input,Exponent,FOLLOW_Exponent_in_number197);  
                    stream_Exponent.add(Exponent9);


                    }
                    break;

            }



            // AST REWRITE
            // elements: Number, Exponent
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 76:4: -> ^( NUMBER Number ( Exponent )? )
            {
                // nofs\\restfs\\json\\json.g:76:7: ^( NUMBER Number ( Exponent )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(NUMBER, "NUMBER"), root_1);

                adaptor.addChild(root_1, stream_Number.nextNode());
                // nofs\\restfs\\json\\json.g:76:23: ( Exponent )?
                if ( stream_Exponent.hasNext() ) {
                    adaptor.addChild(root_1, stream_Exponent.nextNode());

                }
                stream_Exponent.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "number"

    public static class object_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "object"
    // nofs\\restfs\\json\\json.g:79:1: object : '{' members '}' -> ^( OBJECT members ) ;
    public final jsonParser.object_return object() throws RecognitionException {
        jsonParser.object_return retval = new jsonParser.object_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal10=null;
        Token char_literal12=null;
        jsonParser.members_return members11 = null;


        CommonTree char_literal10_tree=null;
        CommonTree char_literal12_tree=null;
        RewriteRuleTokenStream stream_24=new RewriteRuleTokenStream(adaptor,"token 24");
        RewriteRuleTokenStream stream_25=new RewriteRuleTokenStream(adaptor,"token 25");
        RewriteRuleSubtreeStream stream_members=new RewriteRuleSubtreeStream(adaptor,"rule members");
        try {
            // nofs\\restfs\\json\\json.g:79:8: ( '{' members '}' -> ^( OBJECT members ) )
            // nofs\\restfs\\json\\json.g:79:10: '{' members '}'
            {
            char_literal10=(Token)match(input,24,FOLLOW_24_in_object222);  
            stream_24.add(char_literal10);

            pushFollow(FOLLOW_members_in_object224);
            members11=members();

            state._fsp--;

            stream_members.add(members11.getTree());
            char_literal12=(Token)match(input,25,FOLLOW_25_in_object226);  
            stream_25.add(char_literal12);



            // AST REWRITE
            // elements: members
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 80:4: -> ^( OBJECT members )
            {
                // nofs\\restfs\\json\\json.g:80:7: ^( OBJECT members )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(OBJECT, "OBJECT"), root_1);

                adaptor.addChild(root_1, stream_members.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "object"

    public static class array_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "array"
    // nofs\\restfs\\json\\json.g:83:1: array : '[' elements ']' -> ^( ARRAY elements ) ;
    public final jsonParser.array_return array() throws RecognitionException {
        jsonParser.array_return retval = new jsonParser.array_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token char_literal13=null;
        Token char_literal15=null;
        jsonParser.elements_return elements14 = null;


        CommonTree char_literal13_tree=null;
        CommonTree char_literal15_tree=null;
        RewriteRuleTokenStream stream_26=new RewriteRuleTokenStream(adaptor,"token 26");
        RewriteRuleTokenStream stream_27=new RewriteRuleTokenStream(adaptor,"token 27");
        RewriteRuleSubtreeStream stream_elements=new RewriteRuleSubtreeStream(adaptor,"rule elements");
        try {
            // nofs\\restfs\\json\\json.g:83:7: ( '[' elements ']' -> ^( ARRAY elements ) )
            // nofs\\restfs\\json\\json.g:83:9: '[' elements ']'
            {
            char_literal13=(Token)match(input,26,FOLLOW_26_in_array247);  
            stream_26.add(char_literal13);

            pushFollow(FOLLOW_elements_in_array249);
            elements14=elements();

            state._fsp--;

            stream_elements.add(elements14.getTree());
            char_literal15=(Token)match(input,27,FOLLOW_27_in_array251);  
            stream_27.add(char_literal15);



            // AST REWRITE
            // elements: elements
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 84:4: -> ^( ARRAY elements )
            {
                // nofs\\restfs\\json\\json.g:84:7: ^( ARRAY elements )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(ARRAY, "ARRAY"), root_1);

                adaptor.addChild(root_1, stream_elements.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "array"

    public static class elements_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "elements"
    // nofs\\restfs\\json\\json.g:87:1: elements : value ( COMMA value )* ;
    public final jsonParser.elements_return elements() throws RecognitionException {
        jsonParser.elements_return retval = new jsonParser.elements_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA17=null;
        jsonParser.value_return value16 = null;

        jsonParser.value_return value18 = null;


        CommonTree COMMA17_tree=null;

        try {
            // nofs\\restfs\\json\\json.g:87:9: ( value ( COMMA value )* )
            // nofs\\restfs\\json\\json.g:87:11: value ( COMMA value )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_value_in_elements271);
            value16=value();

            state._fsp--;

            adaptor.addChild(root_0, value16.getTree());
            // nofs\\restfs\\json\\json.g:87:17: ( COMMA value )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==COMMA) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // nofs\\restfs\\json\\json.g:87:18: COMMA value
            	    {
            	    COMMA17=(Token)match(input,COMMA,FOLLOW_COMMA_in_elements274); 
            	    pushFollow(FOLLOW_value_in_elements277);
            	    value18=value();

            	    state._fsp--;

            	    adaptor.addChild(root_0, value18.getTree());

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "elements"

    public static class members_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "members"
    // nofs\\restfs\\json\\json.g:90:1: members : pair ( COMMA pair )* ;
    public final jsonParser.members_return members() throws RecognitionException {
        jsonParser.members_return retval = new jsonParser.members_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token COMMA20=null;
        jsonParser.pair_return pair19 = null;

        jsonParser.pair_return pair21 = null;


        CommonTree COMMA20_tree=null;

        try {
            // nofs\\restfs\\json\\json.g:90:9: ( pair ( COMMA pair )* )
            // nofs\\restfs\\json\\json.g:90:11: pair ( COMMA pair )*
            {
            root_0 = (CommonTree)adaptor.nil();

            pushFollow(FOLLOW_pair_in_members289);
            pair19=pair();

            state._fsp--;

            adaptor.addChild(root_0, pair19.getTree());
            // nofs\\restfs\\json\\json.g:90:16: ( COMMA pair )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==COMMA) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // nofs\\restfs\\json\\json.g:90:17: COMMA pair
            	    {
            	    COMMA20=(Token)match(input,COMMA,FOLLOW_COMMA_in_members292); 
            	    pushFollow(FOLLOW_pair_in_members295);
            	    pair21=pair();

            	    state._fsp--;

            	    adaptor.addChild(root_0, pair21.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "members"

    public static class pair_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "pair"
    // nofs\\restfs\\json\\json.g:93:1: pair : String ':' value -> ^( FIELD String value ) ;
    public final jsonParser.pair_return pair() throws RecognitionException {
        jsonParser.pair_return retval = new jsonParser.pair_return();
        retval.start = input.LT(1);

        CommonTree root_0 = null;

        Token String22=null;
        Token char_literal23=null;
        jsonParser.value_return value24 = null;


        CommonTree String22_tree=null;
        CommonTree char_literal23_tree=null;
        RewriteRuleTokenStream stream_String=new RewriteRuleTokenStream(adaptor,"token String");
        RewriteRuleTokenStream stream_28=new RewriteRuleTokenStream(adaptor,"token 28");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // nofs\\restfs\\json\\json.g:93:6: ( String ':' value -> ^( FIELD String value ) )
            // nofs\\restfs\\json\\json.g:93:8: String ':' value
            {
            String22=(Token)match(input,String,FOLLOW_String_in_pair310);  
            stream_String.add(String22);

            char_literal23=(Token)match(input,28,FOLLOW_28_in_pair312);  
            stream_28.add(char_literal23);

            pushFollow(FOLLOW_value_in_pair314);
            value24=value();

            state._fsp--;

            stream_value.add(value24.getTree());


            // AST REWRITE
            // elements: value, String
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 94:4: -> ^( FIELD String value )
            {
                // nofs\\restfs\\json\\json.g:94:7: ^( FIELD String value )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FIELD, "FIELD"), root_1);

                adaptor.addChild(root_1, stream_String.nextNode());
                adaptor.addChild(root_1, stream_value.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }

        catch (RecognitionException e) {
        throw e;
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "pair"

    // Delegated rules


 

    public static final BitSet FOLLOW_string_in_value107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_value112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_object_in_value117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_array_in_value122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_value127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_value136 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_value145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_String_in_string160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Number_in_number188 = new BitSet(new long[]{0x0000000000008002L});
    public static final BitSet FOLLOW_Exponent_in_number197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_object222 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_members_in_object224 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_object226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_array247 = new BitSet(new long[]{0x0000000005E06000L});
    public static final BitSet FOLLOW_elements_in_array249 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_array251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_elements271 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_elements274 = new BitSet(new long[]{0x0000000005E06000L});
    public static final BitSet FOLLOW_value_in_elements277 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_pair_in_members289 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_members292 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_pair_in_members295 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_String_in_pair310 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_pair312 = new BitSet(new long[]{0x0000000005E06000L});
    public static final BitSet FOLLOW_value_in_pair314 = new BitSet(new long[]{0x0000000000000002L});

}