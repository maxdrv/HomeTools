package apps.cards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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

public class CardsPdfPrinter {

    private static final int LINE_SIZE_HEADER = 16;
    private static final int LINE_SIZE = 10;
    private static final int IMPACT_MULTIPLIER = LINE_SIZE + 2;
    private static final int MINIMAL_CARD_IMPACT = 10;
    private static final int AVG_CHARS_IN_LINE = 74;
    private static final int SINGLE_COLUMN = 1;
    private static final String CYRILLIC_FONT_NAME = "/fonts/DejaVuSans.ttf";
    private static final String CYRILLIC_FONT_ENCODING = "cp1251";
    private static final Font HEADER_FONT = FontFactory.getFont(CYRILLIC_FONT_NAME, CYRILLIC_FONT_ENCODING, BaseFont.EMBEDDED, LINE_SIZE_HEADER);
    private static final Font TEXT_FONT = FontFactory.getFont(CYRILLIC_FONT_NAME, CYRILLIC_FONT_ENCODING, BaseFont.EMBEDDED, LINE_SIZE);

    public void print(Path dest, CardList cards) {
        try {
            workInternal(dest, cards);
        } catch (IOException | DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void workInternal(Path file, CardList cards) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, Files.newOutputStream(file));
        document.open();

        for (Card card : cards.getContent()) {
            String upperSideContent = card.getTopic() + "\n" + card.getQuestion();

            long impact = Math.max(calculateImpact(upperSideContent), calculateImpact(card.getAnswer()));
            long cellSize = Math.max(impact * IMPACT_MULTIPLIER, MINIMAL_CARD_IMPACT * IMPACT_MULTIPLIER);

            PdfPCell upperCell = new PdfPCell(new Phrase(upperSideContent, HEADER_FONT));
            upperCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            upperCell.setVerticalAlignment(Element.ALIGN_CENTER);
            upperCell.setFixedHeight(cellSize);

            PdfPCell lowerCell = new PdfPCell(new Phrase(card.getAnswer(), TEXT_FONT));
            lowerCell.setFixedHeight(cellSize);
            lowerCell.setRotation(180);

            PdfPTable table = new PdfPTable(SINGLE_COLUMN);
            table.setKeepTogether(true);
            table.addCell(upperCell);
            table.addCell(lowerCell);

            document.add(table);
            document.add(new DottedLineSeparator());
        }

        document.close();
    }

    private static long calculateImpact(String text) {
        String[] lines = text.split("\n");
        return Arrays.stream(lines)
                .mapToInt(line -> line.length() / AVG_CHARS_IN_LINE + 1)
                .sum();
    }

}
