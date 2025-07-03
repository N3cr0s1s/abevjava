package hu.piller.enykp.interfaces;

public interface IENYKComponent {
   String FEATURE_ABEV_MASK = "abev_mask";
   String FEATURE_ABEV_SUBSCRIPT_HEIGHT = "abev_subscript_height";
   String FEATURE_ABEV_SUBSCRIPT_COLOR = "abev_subscript_color";
   String FEATURE_ABEV_OVERRIDE_MODE = "abev_override_mode";
   String FEATURE_ABEV_INPUT_RULES = "irids";
   String FEATURE_VISIBLE = "visible";
   String FEATURE_TOOLTIP = "tooltip";
   String FEATURE_ENABLED = "enabled";
   String FEATURE_DATA_TYPE = "data_type";
   String FEATURE_DATATYPE = "datatype";
   String FEATURE_BORDER_COLOR = "border_color";
   String FEATURE_BORDER_WIDTH = "border_width";
   String FEATURE_BORDER_SIDES = "border_sides";
   String FEATURE_DISABLED_FG_COLOR = "disabled_fg_color";
   String FEATURE_DISABLED_BG_COLOR = "disabled_bg_color";
   String FEATURE_VISIBLE_ON_PRINT = "visible_on_print";
   String FEATURE_FONT = "font";
   String FEATURE_FONT_COLOR = "font_color";
   String FEATURE_ALIGNMENT = "alignment";
   String FEATURE_TEXTAREA_CONTENT = "textAreaContent";
   String FEATURE_CHAR_RECT_WIDTH = "char_rect_width";
   String FEATURE_DELIMITER_WIDTH = "delimiter_width";
   String FEATURE_DELIMITER_HEIGHT = "delimiter_height";
   String FEATURE_CHAR_RECT_DISTANCE = "char_rect_distance";
   String FEATURE_DRAW_CARET_OVER_LENGTH = "draw_caret_over_length";
   String FEATURE_NOT_FRAMEABLE_CHARS = "not_frameable_chars";
   String FEATURE_OUTER_OPAQUE = "outer_opaque";
   String FEATURE_OUTER_BACKGROUND = "outer_background";
   String FEATURE_RECT_MASK = "rect_mask";
   String FEATURE_MAX_LENGTH = "max_length";
   Integer DATATYPE_STRING = new Integer(1);
   Integer DATATYPE_NUMBER = new Integer(2);
   String FEATURE_LINE_WRAP = "line_wrap";
   String FEATURE_WRAP_STYLE_WORD = "wrap_style_word";

   int getZoom();
}
