package apps.cards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import util.ListUtil;

public class CardsPdfPrinter {

    private static final int LINE_SIZE_HEADER = 16;
    private static final int LINE_SIZE = 10;
    private static final int IMPACT_MULTIPLIER = LINE_SIZE + 2;
    private static final int MIN_LIMIT_CARD_IMPACT = 6;
    private static final int MIN_CELL_HEIGHT = MIN_LIMIT_CARD_IMPACT * IMPACT_MULTIPLIER;
    private static final int AVG_CHARS_IN_LINE = 74;
    private static final int TRANSLATION_SIZE_LIMIT_BEFORE_DROP_TO_TWO_CARDS = 30;
    private static final int SINGLE_COLUMN = 1;
    private static final String CYRILLIC_FONT_NAME = "/fonts/DejaVuSans.ttf";
    private static final String CYRILLIC_FONT_ENCODING = "cp1251";
    private static final Font HEADER_FONT = FontFactory.getFont(CYRILLIC_FONT_NAME, CYRILLIC_FONT_ENCODING, BaseFont.EMBEDDED, LINE_SIZE_HEADER);
    private static final Font TEXT_FONT = FontFactory.getFont(CYRILLIC_FONT_NAME, CYRILLIC_FONT_ENCODING, BaseFont.EMBEDDED, LINE_SIZE);

    private final CardUIType cardUIType;

    public CardsPdfPrinter(CardUIType cardUIType) {
        this.cardUIType = cardUIType;
    }

    public void print(Path dest, CardList cards) {
        Document document = null;
        try {
            document = new Document();
            PdfWriter.getInstance(document, Files.newOutputStream(dest));
            document.open();

            switch (cardUIType) {
                case QUESTION: printQuestions(document, cards); break;
                case TRANSLATION: printTranslation(document, cards); break;
                case TRANSLATION_V2: printTranslationsV2(document, cards); break;
                default: throw new RuntimeException("NOT IMPLEMENTED TYPE OF UI FOR CARDS " + cardUIType);
            }
        } catch (IOException | DocumentException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private void printTranslationsV2(Document document, CardList cards) throws IOException, DocumentException {
        TwoLists twoLists = splitInto3And2(cards);

        List<WordPair> threeItems = twoLists.threeItems;
        List<WordPair> twoItems = twoLists.twoItems;
        twoSidedPage(document, threeItems, AmountOfColumns.N3);
        twoSidedPage(document, twoItems, AmountOfColumns.N2);
    }

    private void twoSidedPage(Document document, List<WordPair> content, AmountOfColumns amountOfColumns) throws DocumentException {
        int lineCountOnPage = 10;

        List<LineOfWordPairs> linesOfWords = ListUtil.listsOfSize(content, amountOfColumns.amount, CardsPdfPrinter::emptyWordPair).stream()
                .map(LineOfWordPairs::new)
                .collect(Collectors.toList());
        List<PageOfWordPairs> pages = ListUtil.listsOfSize(linesOfWords, lineCountOnPage, () -> emptyLine(amountOfColumns)).stream()
                .map(PageOfWordPairs::new)
                .collect(Collectors.toList());

        for (PageOfWordPairs page : pages) {
            for (LineOfWordPairs line : page.lines) {
                PdfPTable frontLine = getFrontLine(line.wordPairs, amountOfColumns);
                document.add(frontLine);
            }
            for (LineOfWordPairs line : page.lines) {
                PdfPTable backLine = getBackLine(line.wordPairs, amountOfColumns);
                document.add(backLine);
            }
        }
    }

    private PdfPTable getFrontLine(List<WordPair> partition, AmountOfColumns amountOfColumns) {
        PdfPTable table = new PdfPTable(amountOfColumns.amount);
        table.setKeepTogether(true);
        for (WordPair wordPair : partition) {
            table.addCell(wordPair.upper);
        }
        return table;
    }

    private PdfPTable getBackLine(List<WordPair> partition, AmountOfColumns amountOfColumns) {
        PdfPTable table = new PdfPTable(amountOfColumns.amount);
        table.setKeepTogether(true);
        for (int i = partition.size() - 1; i >= 0; i--) {
            table.addCell(partition.get(i).lower);
        }
        return table;
    }

    private void printTranslation(Document document, CardList cards) throws DocumentException {
        TwoLists twoLists = splitInto3And2(cards);
        addTable(document, twoLists.threeItems, AmountOfColumns.N3);
        addTable(document, twoLists.twoItems, AmountOfColumns.N2);
    }

    private TwoLists splitInto3And2(CardList cards) {
        List<WordPair> threeItems = new ArrayList<>();
        List<WordPair> twoItems = new ArrayList<>();

        cards.getContent()
                .forEach(card -> {
                    String upperSideContent = card.getQuestion();
                    String lowerSideContent = card.getAnswer();

                    PdfPCell upperCell = cell(upperSideContent, MIN_CELL_HEIGHT, false);
                    PdfPCell lowerCell = cell(lowerSideContent, MIN_CELL_HEIGHT, true);
                    WordPair wordPair = new WordPair(upperCell, lowerCell);

                    if (upperSideContent.length() > TRANSLATION_SIZE_LIMIT_BEFORE_DROP_TO_TWO_CARDS ||
                            lowerSideContent.length() > TRANSLATION_SIZE_LIMIT_BEFORE_DROP_TO_TWO_CARDS
                    ) {
                        twoItems.add(wordPair);
                    } else {
                        threeItems.add(wordPair);
                    }
                });

        return new TwoLists(threeItems, twoItems);
    }

    private void addTable(Document document, List<WordPair> content, AmountOfColumns amountOfColumns) throws DocumentException {
        List<List<WordPair>> partitionsOfThree = ListUtil.partition(content, amountOfColumns.amount);
        for (List<WordPair> partition : partitionsOfThree) {
            PdfPTable table = new PdfPTable(amountOfColumns.amount);
            table.setKeepTogether(true);
            for (WordPair wordPair : partition) {
                table.addCell(wordPair.upper);
            }
            for (WordPair wordPair : partition) {
                table.addCell(wordPair.lower);
            }
            document.add(new DottedLineSeparator());
            document.add(table);
        }
    }

    private void printQuestions(Document document, CardList cards) throws IOException, DocumentException {
        List<Card> content = cards.getContent();
        for (Card card : content) {
            String upperSideContent = card.getTopic() + "\n" + card.getQuestion();

            long impact = Math.max(calculateImpact(upperSideContent), calculateImpact(card.getAnswer()));
            long cellHeight = Math.max(impact * IMPACT_MULTIPLIER, MIN_CELL_HEIGHT);

            PdfPCell upperCell = new PdfPCell(new Phrase(upperSideContent, HEADER_FONT));
            upperCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            upperCell.setVerticalAlignment(Element.ALIGN_CENTER);
            upperCell.setFixedHeight(cellHeight);

            PdfPCell lowerCell = new PdfPCell(new Phrase(card.getAnswer(), TEXT_FONT));
            lowerCell.setFixedHeight(cellHeight);
            lowerCell.setRotation(180);

            PdfPTable table = new PdfPTable(SINGLE_COLUMN);
            table.setKeepTogether(true);
            table.addCell(upperCell);
            table.addCell(lowerCell);

            document.add(table);
            document.add(new DottedLineSeparator());
        }
    }

    private static long calculateImpact(String text) {
        String[] lines = text.split("\n");
        return Arrays.stream(lines)
                .mapToInt(line -> line.length() / AVG_CHARS_IN_LINE + 1)
                .sum();
    }

    private static PdfPCell emptyCell() {
        return cell("", MIN_CELL_HEIGHT, false);
    }

    private static PdfPCell cell(String text, long height, boolean rotate) {
        PdfPCell cell = new PdfPCell(new Phrase(text, TEXT_FONT));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setFixedHeight(height);
        if (rotate) {
            cell.setRotation(180);
        }
        return cell;
    }

    private static class TwoLists {
        List<WordPair> threeItems;
        List<WordPair> twoItems;

        public TwoLists(List<WordPair> threeItems, List<WordPair> twoItems) {
            this.threeItems = threeItems;
            this.twoItems = twoItems;
        }
    }

    private static class WordPair {
        PdfPCell upper;
        PdfPCell lower;

        public WordPair(PdfPCell upper, PdfPCell lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }

    private static class LineOfWordPairs {
        List<WordPair> wordPairs;

        public LineOfWordPairs(List<WordPair> wordPairs) {
            this.wordPairs = wordPairs;
        }
    }

    private static class PageOfWordPairs {
        List<LineOfWordPairs> lines;

        public PageOfWordPairs(List<LineOfWordPairs> lines) {
            this.lines = lines;
        }
    }

    private static WordPair emptyWordPair() {
        return new WordPair(emptyCell(), emptyCell());
    }

    private static LineOfWordPairs emptyLine(AmountOfColumns amountOfColumns) {
        List<WordPair> words = new ArrayList<>();
        for (int i = 0; i < amountOfColumns.amount; i++) {
            words.add(emptyWordPair());
        }
        return new LineOfWordPairs(words);
    }

    private enum AmountOfColumns {
        N2(2),
        N3(3);

        final int amount;

        AmountOfColumns(int amount) {
            this.amount = amount;
        }
    }

}
