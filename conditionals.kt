data class XCoordinates(val l: Int, val r: Int)
data class YCoordinates(val u: Int, val d: Int)

/* w = width, h = height, tH = TotalHeight, tW = TotalWidth
   Short names were used instead of more descriptive variable names
   to avoid clutter. */
class conditionalChecks(w: Int, h: Int, totalHeight: Int, totalWidth: Int) {
    private var tH = totalHeight
    private var tW = totalWidth
    private var up = 0
    private var down = 0
    private var left = 0
    private var right = 0

    fun Yconditions(h: Int): YCoordinates {
        if (h == 0 || h == tH - 1) {
            if (h == 0) {
                up = h; down = h + 2
            } else if (h == tH -1) {
                up = h - 2; down = h
            }
        }
        else {
            up = h + 1; down = h -1
        }
        return YCoordinates(up, down)
    }

    fun Xconditions(w: Int): XCoordinates {
        if (w == 0 || w == tW - 1) {
            if (w == 0) {
                left = w; right = w + 2
            } else if (w == tW -1) {
                left = w - 2; right = w
            }
        }
        else {
            left = w - 1; right = w + 1
        }
        return XCoordinates(left, right)
    }
}
