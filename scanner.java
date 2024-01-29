public class scanner {

    private String line;
    private int linenumber;
    private int pointer = 0;
    private int maxpointer;
    private char c;

    private final String nonzero = "123456789";
    private final String digit = "0123456789";
    private final String letter = "abcdefghijklmnopqrstuvwxyz";
    private final String error = "@#$%^\" \\ \'?";
    private final String puncops = "+<>+-*/=|&!(){}[];:.,";
    private final String reserved = "if then else integer float void public private func var struct while read write return self inherits let impl ";
    private final char zero = '0';
    private final String integer = "integer";
    private final String id = "id";
    private final String flo = "float";
    private final String invalid = "invalid id";
    private final String keyword = "keyword";
    private final String booleanop = "booleanoperator"; // ==, <>, <, >, <=, >=, |, &, !
    private final String operator = "operator"; // +, -, *, /, =
    private final String punctuation = "punctuation"; // (){}[];:.->
    private final String spacing = "\\n \\t";

    public scanner(String line) {
        this.line = line;
        this.maxpointer = line.length();
        this.linenumber = 0;
        this.c = line.charAt(this.pointer);

    }

    public Token nextToken() {
        Token token = new Token();
        // looping through input
        outerloop: while (true) {
            // zero
            if (c == zero) {
                token = new Token(integer, String.valueOf(c), String.valueOf(linenumber));
                nextChar();
                break;

                // blank space just go to next char
            } else if (c == ' ') {
                nextChar();
                continue;

                // invalid underscore id
            } else if (c == '_') {
                String lex = String.valueOf(c);
                nextChar();
                while ((letter.contains(String.valueOf(c)) | digit.contains(String.valueOf(c)) | c != ' ')
                        & pointer < maxpointer - 1) {
                    lex = lex.concat(String.valueOf(c));
                    nextChar();
                }
                if (pointer == maxpointer - 1) {
                    lex = lex.concat(String.valueOf(c));
                    token = new Token(invalid, lex, String.valueOf(linenumber));
                    break;
                } else {
                    backupChar();
                    token = new Token(invalid, lex, String.valueOf(linenumber));
                    break;
                }

                // number (not zero)
            } else if (nonzero.contains(String.valueOf(c))) {
                String lex = String.valueOf(c);
                nextChar();

                // continue to next char while we see numbers
                while (digit.contains(String.valueOf(c)) & pointer < maxpointer - 1) {
                    lex = lex.concat(String.valueOf(c));
                    nextChar();
                }
                // floats
                if (c == '.') {
                    lex = lex.concat(String.valueOf(c));
                    nextChar();
                    if (digit.contains(String.valueOf(c))) {
                        lex = lex.concat(String.valueOf(c));
                        nextChar();
                        while (digit.contains(String.valueOf(c)) & pointer < maxpointer - 1) {
                            lex = lex.concat(String.valueOf(c));
                            nextChar();
                        }
                        // make sure no trailing zeroes
                        if (lex.charAt(lex.length() - 1) == '0') {
                            backupChar();
                            lex = lex.substring(0, lex.length() - 1);
                            while (c == '0') {
                                backupChar();
                                lex = lex.substring(0, lex.length() - 1);
                            }
                            token = new Token(flo, lex, String.valueOf(linenumber));
                            break;

                            // if we have floating point with e
                        } else if (c == 'e') {
                            lex = lex.concat(String.valueOf(c));
                            if (pointer < maxpointer - 1) {
                                nextChar();
                            } else {
                                token = new Token(invalid, lex, String.valueOf(linenumber));
                                break;
                            }
                            // float with e0 ending
                            if (c == '0') {
                                lex = lex.concat(String.valueOf(c));
                                token = new Token(flo, lex, String.valueOf(linenumber));
                                break;

                                // float with e-... or e+... or e...
                            } else if (c == '+' | c == '-' | nonzero.contains(String.valueOf(c))) {
                                lex = lex.concat(String.valueOf(c));
                                System.out.println(lex);
                                nextChar();
                                if (nonzero.contains(String.valueOf(c))
                                        | nonzero.contains(String.valueOf(lex.charAt(lex.length() - 1)))) {
                                    while (digit.contains(String.valueOf(c)) & pointer < maxpointer - 1) {
                                        lex = lex.concat(String.valueOf(c));
                                        System.out.println(lex);
                                        nextChar();
                                    }
                                    if (pointer == maxpointer - 1) {
                                        if (digit.contains(String.valueOf(c))) {
                                            lex = lex.concat(String.valueOf(c));
                                            token = new Token(flo, lex, String.valueOf(linenumber));
                                            break;
                                        } else {
                                            token = new Token(flo, lex, String.valueOf(linenumber));
                                            System.out.println(lex);
                                            break;
                                        }

                                    } else {
                                        token = new Token(flo, lex, String.valueOf(linenumber));
                                        break;
                                    }

                                } else {
                                    backupChar();
                                    lex = lex.substring(0, lex.length() - 1);
                                    token = new Token(invalid, lex, String.valueOf(linenumber));
                                    break;
                                }
                            } else {
                                token = new Token(flo, lex, String.valueOf(linenumber));
                                break;
                            }
                        } else {
                            String lasttwo = "" + lex.charAt((lex.length()) - 2) + lex.charAt((lex.length() - 1));
                            // float that ends in .0
                            if (lasttwo.equals(".0")) {
                                token = new Token(flo, lex, String.valueOf(linenumber));
                                break;

                                // float with no trailing zeroes
                            } else if (lex.charAt(lex.length() - 1) == '0') {
                                backupChar();
                                lex = lex.substring(0, lex.length() - 1);
                                while (c == '0') {
                                    backupChar();
                                    lex = lex.substring(0, lex.length() - 1);
                                }
                                token = new Token(flo, lex, String.valueOf(linenumber));
                                break;
                            } else {
                                token = new Token(flo, lex, String.valueOf(linenumber));
                                break;
                            }
                        }

                    } else {
                        backupChar();
                        lex = lex.substring(0, lex.length() - 1);
                        token = new Token(invalid, lex, String.valueOf(linenumber));
                        break;
                    }
                } else {
                    if (pointer == maxpointer - 1) {
                        lex = lex.concat(String.valueOf(c));
                        token = new Token(integer, lex, String.valueOf(linenumber));
                        break;
                    } else {
                        backupChar();
                        lex = lex.substring(0, lex.length() - 1);
                        token = new Token(integer, lex, String.valueOf(linenumber));
                        break;
                    }
                }
                // ids and keywords
            } else if (letter.contains(String.valueOf(c))) {
                String lex = String.valueOf(c);
                nextChar();
                while ((letter.contains(String.valueOf(c)) | digit.contains(String.valueOf(c)) | c == '_') & pointer < maxpointer-1) {
                    lex = lex.concat(String.valueOf(c));
                    nextChar();
                }
                if ((pointer == maxpointer-1) & (letter.contains(String.valueOf(c)) | digit.contains(String.valueOf(c)) | c == '_')) {
                    lex = lex.concat(String.valueOf(c));
                    token = new Token(id, lex, String.valueOf(linenumber));
                    break;
                } else if (c == ' ') {
                    if (lex.equals("if") | lex.equals("then") | lex.equals("else") | lex.equals("integer") | lex.equals("float") | lex.equals("void") | lex.equals("public") | lex.equals("private") | lex.equals("func") | lex.equals("var") | lex.equals("struct") | lex.equals("while") | lex.equals("read") | lex.equals("write") | lex.equals("return")| lex.equals("self") | lex.equals("inherits")| lex.equals("let") | lex.equals("impl")) {
                        System.out.println("in here");
                        token = new Token(keyword, lex, String.valueOf(linenumber));
                        break;
                    }
                } else {
                    backupChar();
                    lex = lex.substring(0, lex.length() - 1);
                    token = new Token(id, lex, String.valueOf(linenumber));
                    break;
                }
                // operators and punctuation
            } else if (puncops.contains(String.valueOf(c))) {
                String lex = String.valueOf(c);
                switch (c) {
                    case '=':
                        nextChar();
                        lex = lex.concat(String.valueOf(c));
                        if (lex.equals("==")) {
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        } else {
                            backupChar();
                            lex = lex.substring(0, lex.length() - 1);
                            token = new Token(operator, lex, String.valueOf(linenumber));
                            break outerloop;
                        }
                    case '<':
                        nextChar();
                        lex = lex.concat(String.valueOf(c));
                        if (lex.equals("<>")) {
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        } else if (lex.equals("<=")) {
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        } else {
                            backupChar();
                            lex.substring(0, lex.length() - 1);
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        }
                    case '>':
                        nextChar();
                        lex = lex.concat(String.valueOf(c));
                        if (lex.equals(">=")) {
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        } else {
                            backupChar();
                            lex.substring(0, lex.length() - 1);
                            token = new Token(booleanop, lex, String.valueOf(linenumber));
                            break outerloop;
                        }
                    case '-':
                        nextChar();
                        lex = lex.concat(String.valueOf(c));
                        if (lex.equals("->")) {
                            token = new Token(punctuation, lex, String.valueOf(linenumber));
                            break outerloop;
                        } else {
                            backupChar();
                            lex.substring(0, lex.length() - 1);
                            token = new Token(operator, lex, String.valueOf(linenumber));
                            break outerloop;
                        }
                    case '+':
                        token = new Token(operator, lex, String.valueOf(linenumber));
                        break outerloop;
                    case '*':
                        token = new Token(operator, lex, String.valueOf(linenumber));
                        break outerloop;
                    case '/':
                        nextChar();
                        lex = lex.concat(String.valueOf(c));
                        if (lex.equals("//")) {
                            nextChar();
                            while (c != '\t') {
                                nextChar();
                            }
                            nextChar();
                            continue;
                        } else if (lex.equals("/*")) {
                            nextChar();
                            String closeComment = String.valueOf(c);
                            nextChar();
                            closeComment.concat(String.valueOf(c));
                            while (!closeComment.equals("*/")) {
                                nextChar();
                                closeComment = String.valueOf(c);
                                nextChar();
                                closeComment.concat(String.valueOf(c));
                            }
                            nextChar();
                            continue;
                        } else {
                            backupChar();
                            lex = lex.substring(0, lex.length() - 1);
                            token = new Token(operator, lex, String.valueOf(linenumber));
                            break outerloop;
                        }
                    default:
                        break;
                }

            } else if (error.contains(String.valueOf(c))) {
                String lex = String.valueOf(c);
                token = new Token(error, lex, String.valueOf(linenumber));
            } else if (spacing.contains(String.valueOf(c))) {
                nextChar();
                continue;
            }
        }
        return token;
    }

    private void nextChar() {
        if (pointer < maxpointer - 1) {
            this.pointer++;
            this.c = line.charAt(this.pointer);
        } else {

        }
    }

    private void backupChar() {
        this.pointer--;
        this.c = line.charAt(this.pointer);
    }

}
