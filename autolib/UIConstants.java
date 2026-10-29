package autolib;

import java.awt.*;

public class UIConstants {
    // ── Portal / Dark screen ──────────────────────────────────────────────────
    public static final Color PORTAL_BG = new Color(13, 17, 23);
    public static final Color PORTAL_CARD_BG = new Color(22, 27, 34);
    public static final Color PORTAL_BTN_BG = new Color(30, 37, 46);
    public static final Color PORTAL_BTN_HOVER = new Color(40, 50, 63);
    public static final Color PORTAL_BORDER = new Color(48, 58, 72);

    // ── Sidebar ───────────────────────────────────────────────────────────────
    public static final Color SIDEBAR_BG = new Color(18, 22, 36);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(204, 41, 54);
    public static final Color SIDEBAR_HOVER_BG = new Color(30, 36, 54);
    public static final Color SIDEBAR_TEXT = new Color(170, 178, 200);
    public static final Color SIDEBAR_ACTIVE_TXT = Color.WHITE;
    public static final Color SIDEBAR_SECTION = new Color(90, 100, 125);
    public static final Color SIDEBAR_LOGO_TXT = Color.WHITE;

    // ── Top-bar ───────────────────────────────────────────────────────────────
    public static final Color TOPBAR_BG = Color.WHITE;
    public static final Color TOPBAR_BORDER = new Color(230, 233, 240);

    // ── Content area ──────────────────────────────────────────────────────────
    public static final Color CONTENT_BG = new Color(244, 246, 250);
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_BORDER = new Color(228, 232, 240);

    // ── Typography ────────────────────────────────────────────────────────────
    public static final Color TEXT_PRIMARY = new Color(20, 25, 38);
    public static final Color TEXT_SECONDARY = new Color(90, 100, 125);
    public static final Color TEXT_MUTED = new Color(148, 158, 178);
    public static final Color TEXT_WHITE = Color.WHITE;

    // ── Accent ────────────────────────────────────────────────────────────────
    public static final Color ACCENT_RED = new Color(204, 41, 54);
    public static final Color ACCENT_RED_DARK = new Color(170, 28, 40);
    public static final Color ACCENT_RED_LIGHT = new Color(254, 226, 228);
    public static final Color ACCENT_GREEN = new Color(34, 197, 94);
    public static final Color ACCENT_GREEN_BG = new Color(220, 252, 231);
    public static final Color ACCENT_ORANGE = new Color(234, 88, 12);
    public static final Color ACCENT_ORANGE_BG = new Color(255, 237, 213);
    public static final Color ACCENT_AMBER = new Color(202, 138, 4);
    public static final Color ACCENT_AMBER_BG = new Color(254, 249, 195);
    public static final Color ACCENT_BLUE = new Color(59, 130, 246);
    public static final Color ACCENT_BLUE_BG = new Color(219, 234, 254);

    // ── Table ─────────────────────────────────────────────────────────────────
    public static final Color TABLE_HEADER_BG = new Color(248, 250, 252);
    public static final Color TABLE_HEADER_TEXT = new Color(90, 100, 125);
    public static final Color TABLE_ROW_EVEN = Color.WHITE;
    public static final Color TABLE_ROW_ODD = new Color(250, 251, 253);
    public static final Color TABLE_GRID = new Color(232, 236, 243);
    public static final Color TABLE_LINK = new Color(204, 41, 54);

    // ── Input ─────────────────────────────────────────────────────────────────
    public static final Color INPUT_BG = Color.WHITE;
    public static final Color INPUT_BORDER = new Color(210, 216, 230);
    public static final Color INPUT_PLACEHOLDER = new Color(160, 168, 185);

    // ── Dimensions ────────────────────────────────────────────────────────────
    public static final int SIDEBAR_WIDTH = 220;
    public static final int TOPBAR_HEIGHT = 58;
    public static final int CARD_RADIUS = 12;
    public static final int BADGE_RADIUS = 20;

    // ── Fonts (Segoe UI is standard on Windows; falls back to Sans-Serif) ──────
    public static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    public static final Font F_TITLE = font(Font.BOLD, 28);
    public static final Font F_HEADING = font(Font.BOLD, 16);
    public static final Font F_SUBHEAD = font(Font.BOLD, 13);
    public static final Font F_BODY = font(Font.PLAIN, 13);
    public static final Font F_SMALL = font(Font.PLAIN, 11);
    public static final Font F_TINY = font(Font.PLAIN, 10);
    public static final Font F_LOGO = font(Font.BOLD, 20);
    public static final Font F_STAT_NUM = font(Font.BOLD, 30);
    public static final Font F_STAT_LBL = font(Font.PLAIN, 12);
    public static final Font F_NAV = font(Font.PLAIN, 13);
    public static final Font F_NAV_ACTIVE = font(Font.BOLD, 13);
    public static final Font F_BTN = font(Font.BOLD, 13);
    public static final Font F_TABLE_HDR = font(Font.BOLD, 11);
    public static final Font F_TABLE_CELL = font(Font.PLAIN, 13);
}
