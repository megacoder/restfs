grammar json;

options {
	output = AST;
	ASTLabelType=CommonTree;
	language = Java;
}

tokens {
	STRING; NUMBER; OBJECT; FIELD; ARRAY;
	COMMA = ',';
	TRUE; FALSE; NULL;
}

@header {
package nofs.restfs.json;

import java.util.regex.Pattern;

}

@lexer::header {
package nofs.restfs.json;
}

// Optional step: Disable automatic error recovery
@members {
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

}
// Alter code generation so catch-clauses get replace with
// this action.
@rulecatch {
catch (RecognitionException e) {
throw e;
}
} 
value

	: string
	| number
	| object
	| array
	| 'true' -> TRUE
	| 'false' -> FALSE
	| 'null' -> NULL
	;

string 	: String
	  -> ^(STRING String)
	;

// If you want to conform to the RFC, use a validating semantic predicate to check the result.
// You can omit the check if you want. The parser will still recognize valid JSON and it will
// allow numbers with leading zeroes.
// See the second note above for an alternate approach using the tree parser.
// This could be more efficient (e.g. pre-compile the pattern), but I'm going for clarity here.
number	: n=Number {Pattern.matches("(0|(-?[1-9]\\d*))(\\.\\d+)?", n.getText())}?
	    Exponent?
	  -> ^(NUMBER Number Exponent?)
	;

object	: '{' members '}'
	  -> ^(OBJECT members)
	;

array	: '[' elements ']'
	  -> ^(ARRAY elements)
	;

elements: value (COMMA! value)*
	;

members	: pair (COMMA! pair)*
    ;

pair	: String ':' value
	  -> ^(FIELD String value)
	;
	
// Simple, but more permissive than the RFC allows. See number above for a validity check.
Number	: '-'? Digit+ ( '.' Digit+)?;

Exponent: ('e'|'E') '-'? Digit+;

String 	:
	'"' ( EscapeSequence | ~('\u0000'..'\u001f' | '\\' | '\"' ) )* '"'
	;

WS: (' '|'\n'|'\r'|'\t')+ {$channel=HIDDEN;} ; // ignore whitespace

fragment EscapeSequence
    	:   '\\' (UnicodeEscape |'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    	;

fragment UnicodeEscape
	: 'u' HexDigit HexDigit HexDigit HexDigit
	;

fragment HexDigit
	: '0'..'9' | 'A'..'F' | 'a'..'f'
	;

fragment Digit
	: '0'..'9'
	;

