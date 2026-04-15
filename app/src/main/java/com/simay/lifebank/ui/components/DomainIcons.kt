package com.simay.lifebank.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

// Palette — tüm üç yüz beyaz kart arka planında net seçilebilir olacak şekilde
private val IsoTop   = Color(0xFFA8C9E8)  // üst yüz  — orta-açık mavi
private val IsoFront = Color(0xFF4A7EB8)  // ön yüz   — orta mavi
private val IsoSide  = Color(0xFF1E4F8E)  // yan yüz  — koyu lacivert
private val IsoOpen  = Color(0xFF0F2A5A)  // pencere/kapı — çok koyu
private val IsoWhite = Color(0xFFFFFFFF)  // haç/detay

// ─── Primitifler ──────────────────────────────────────────────────────────────

private fun DrawScope.face4(p1: Offset, p2: Offset, p3: Offset, p4: Offset, color: Color) {
    drawPath(Path().apply {
        moveTo(p1.x, p1.y); lineTo(p2.x, p2.y)
        lineTo(p3.x, p3.y); lineTo(p4.x, p4.y); close()
    }, color)
}

private fun DrawScope.face3(p1: Offset, p2: Offset, p3: Offset, color: Color) {
    drawPath(Path().apply {
        moveTo(p1.x, p1.y); lineTo(p2.x, p2.y); lineTo(p3.x, p3.y); close()
    }, color)
}

/**
 * İzometrik koordinat→ekran dönüşümü.
 * origin (ox,oy) = ön-sol-alt köşe
 * r=sağ (cos30,sin30)*s, l=sol (-cos30,sin30)*s, h=yukarı (0,-1)*s
 */
private fun pt(ox: Float, oy: Float, s: Float, r: Float, l: Float, h: Float) =
    Offset(ox + r * s * 0.433f - l * s * 0.433f,
           oy + r * s * 0.25f  + l * s * 0.25f  - h * s * 0.5f)

// ─── Evim ─────────────────────────────────────────────────────────────────────

private fun DrawScope.drawHouse() {
    val s  = size.minDimension * 0.78f
    // bw=bd=1 → simetrik, ox=cx
    val ox = size.width  * 0.5f
    // bottom=oy+0.5s, ridge_top=oy-0.375s → merkez=oy+0.063s → oy=cy-0.063s
    val oy = size.height * 0.5f - 0.063f * s

    fun p(r: Float, l: Float, h: Float) = pt(ox, oy, s, r, l, h)

    // Kutu
    face4(p(0f,0f,1f), p(1f,0f,1f), p(1f,1f,1f), p(0f,1f,1f), IsoTop)   // üst
    face4(p(0f,0f,0f), p(1f,0f,0f), p(1f,0f,1f), p(0f,0f,1f), IsoFront) // ön
    face4(p(1f,0f,0f), p(1f,1f,0f), p(1f,1f,1f), p(1f,0f,1f), IsoSide)  // sağ

    // Çatı
    val ridge = 0.55f + 0.42f           // toplam yükseklik
    val rf = p(0.5f, 0f, ridge)
    val rb = p(0.5f, 1f, ridge)
    face3(p(0f,0f,0.55f), p(1f,0f,0.55f), rf, IsoFront.copy(alpha = 0.85f))      // ön üçgen
    face4(p(1f,0f,0.55f), p(1f,1f,0.55f), rb, rf, IsoSide.copy(alpha = 0.80f))   // sağ
    face4(p(0f,0f,0.55f), rf, rb, p(0f,1f,0.55f), IsoTop.copy(alpha = 0.90f))    // sol

    // Kapı
    face4(p(0.40f,0f,0f), p(0.60f,0f,0f), p(0.60f,0f,0.30f), p(0.40f,0f,0.30f), IsoOpen)

    // Pencere
    face4(p(0.68f,0f,0.28f), p(0.86f,0f,0.28f), p(0.86f,0f,0.46f), p(0.68f,0f,0.46f), IsoOpen)
}

// ─── Aracım ───────────────────────────────────────────────────────────────────

private fun DrawScope.drawCar() {
    val s  = size.minDimension * 0.78f
    // bw=1.1, bd=0.55 → ox = cx - (1.1-0.55)/2 * 0.433s = cx - 0.119s
    val ox = size.width  * 0.5f - 0.119f * s
    // bottom≈oy+0.4125s, cabin_top≈oy-0.325s → center≈oy+0.044s → oy=cy-0.044s
    val oy = size.height * 0.5f - 0.044f * s

    fun p(r: Float, l: Float, h: Float) = pt(ox, oy, s, r, l, h)

    val bw = 1.1f; val bd = 0.55f; val bh = 0.40f

    // Gövde
    face4(p(0f,0f,bh), p(bw,0f,bh), p(bw,bd,bh), p(0f,bd,bh), IsoTop)
    face4(p(0f,0f,0f), p(bw,0f,0f), p(bw,0f,bh), p(0f,0f,bh),  IsoFront)
    face4(p(bw,0f,0f), p(bw,bd,0f), p(bw,bd,bh), p(bw,0f,bh),  IsoSide)

    // Kabin
    val cs = 0.15f; val ce = 0.92f; val ch = 0.30f
    face4(p(cs,0f,bh+ch), p(ce,0f,bh+ch), p(ce,bd,bh+ch), p(cs,bd,bh+ch), IsoTop.copy(alpha = 0.88f))
    face4(p(cs,0f,bh), p(ce,0f,bh), p(ce,0f,bh+ch), p(cs,0f,bh+ch),       IsoFront.copy(alpha = 0.82f))
    face4(p(ce,0f,bh), p(ce,bd,bh), p(ce,bd,bh+ch), p(ce,0f,bh+ch),       IsoSide.copy(alpha = 0.78f))

    // Ön cam
    face4(p(cs+0.06f,0f,bh+0.06f), p(ce-0.06f,0f,bh+0.06f),
          p(ce-0.09f,0f,bh+ch-0.05f), p(cs+0.09f,0f,bh+ch-0.05f),
          IsoOpen.copy(alpha = 0.65f))

    // Tekerlekler (küçük)
    val wr = s * 0.048f
    val w1 = p(0.22f, bd, 0f)
    val w2 = p(0.88f, bd, 0f)
    drawCircle(IsoSide, wr, w1); drawCircle(IsoTop, wr * 0.45f, w1)
    drawCircle(IsoSide, wr, w2); drawCircle(IsoTop, wr * 0.45f, w2)
}

// ─── Seyahatim ────────────────────────────────────────────────────────────────

private fun DrawScope.drawSuitcase() {
    val s  = size.minDimension * 0.78f
    // bw=0.90, bd=0.40 → ox = cx - (0.90-0.40)/2 * 0.433s = cx - 0.108s
    val ox = size.width  * 0.5f - 0.108f * s
    // bottom=oy+(0.9+0.4)*0.25s=oy+0.325s, handle_top=oy-0.395s → center=oy-0.035s → oy=cy+0.035s
    val oy = size.height * 0.5f + 0.035f * s

    fun p(r: Float, l: Float, h: Float) = pt(ox, oy, s, r, l, h)

    val bw = 0.90f; val bd = 0.40f; val bh = 0.72f

    // Gövde
    face4(p(0f,0f,bh), p(bw,0f,bh), p(bw,bd,bh), p(0f,bd,bh), IsoTop)
    face4(p(0f,0f,0f), p(bw,0f,0f), p(bw,0f,bh), p(0f,0f,bh),  IsoFront)
    face4(p(bw,0f,0f), p(bw,bd,0f), p(bw,bd,bh), p(bw,0f,bh),  IsoSide)

    // Yatay kemer şeridi
    val bY = 0.38f; val bT = 0.06f
    face4(p(0f,0f,bY), p(bw,0f,bY), p(bw,0f,bY+bT), p(0f,0f,bY+bT),
          IsoSide.copy(alpha = 0.55f))

    // Sap
    val hi = 0.28f; val hH = 0.13f; val hT = 0.04f
    face4(p(hi,0f,bh), p(hi+hT,0f,bh), p(hi+hT,0f,bh+hH), p(hi,0f,bh+hH), IsoSide)
    face4(p(bw-hi-hT,0f,bh), p(bw-hi,0f,bh), p(bw-hi,0f,bh+hH), p(bw-hi-hT,0f,bh+hH), IsoSide)
    face4(p(hi,0f,bh+hH), p(bw-hi,0f,bh+hH),
          p(bw-hi,0f,bh+hH+hT), p(hi,0f,bh+hH+hT), IsoSide)

    // Köşe perçinleri
    val rR = s * 0.026f
    drawCircle(IsoSide.copy(alpha = 0.6f), rR, p(0.07f, 0f, 0.08f))
    drawCircle(IsoSide.copy(alpha = 0.6f), rR, p(bw-0.07f, 0f, 0.08f))
    drawCircle(IsoSide.copy(alpha = 0.6f), rR, p(0.07f, 0f, bh-0.08f))
    drawCircle(IsoSide.copy(alpha = 0.6f), rR, p(bw-0.07f, 0f, bh-0.08f))
}

// ─── Sağlığım ─────────────────────────────────────────────────────────────────

private fun DrawScope.drawMedKit() {
    val s  = size.minDimension * 0.78f
    // Küp simetrik → ox=cx, oy=cy
    val ox = size.width  * 0.5f
    val oy = size.height * 0.5f

    fun p(r: Float, l: Float, h: Float) = pt(ox, oy, s, r, l, h)

    val b = 0.82f

    face4(p(0f,0f,b), p(b,0f,b), p(b,b,b), p(0f,b,b), IsoTop)
    face4(p(0f,0f,0f), p(b,0f,0f), p(b,0f,b), p(0f,0f,b),  IsoFront)
    face4(p(b,0f,0f), p(b,b,0f), p(b,b,b), p(b,0f,b),      IsoSide)

    // Beyaz haç (ön yüz, belirgin)
    val cr = b * 0.5f; val ch = b * 0.5f; val cW = 0.11f; val cH = 0.31f
    // Dikey
    face4(p(cr-cW/2f,0f,ch-cH/2f), p(cr+cW/2f,0f,ch-cH/2f),
          p(cr+cW/2f,0f,ch+cH/2f), p(cr-cW/2f,0f,ch+cH/2f), IsoWhite)
    // Yatay
    face4(p(cr-cH/2f,0f,ch-cW/2f), p(cr+cH/2f,0f,ch-cW/2f),
          p(cr+cH/2f,0f,ch+cW/2f), p(cr-cH/2f,0f,ch+cW/2f), IsoWhite)

    // Kapak dikişi
    drawLine(IsoSide.copy(alpha = 0.4f),
        p(0f, 0f, b * 0.70f), p(b, 0f, b * 0.70f),
        strokeWidth = s * 0.018f)
}

// ─── Ailem ────────────────────────────────────────────────────────────────────

private fun DrawScope.drawFamilyHome() {
    val s  = size.minDimension * 0.78f
    // bw=1.1, bd=0.85 → ox = cx - (1.1-0.85)/2 * 0.433s = cx - 0.054s
    val ox = size.width  * 0.5f - 0.054f * s
    // bottom≈oy+0.4875s, ridge_top≈oy-0.345s → center≈oy+0.071s → oy=cy-0.071s
    val oy = size.height * 0.5f - 0.071f * s

    fun p(r: Float, l: Float, h: Float) = pt(ox, oy, s, r, l, h)

    val bw = 1.1f; val bd = 0.85f; val bh = 0.48f

    face4(p(0f,0f,bh), p(bw,0f,bh), p(bw,bd,bh), p(0f,bd,bh), IsoTop)
    face4(p(0f,0f,0f), p(bw,0f,0f), p(bw,0f,bh), p(0f,0f,bh),  IsoFront)
    face4(p(bw,0f,0f), p(bw,bd,0f), p(bw,bd,bh), p(bw,0f,bh),  IsoSide)

    val rH = bh + 0.40f
    val rf = p(bw*0.5f, 0f, rH)
    val rb = p(bw*0.5f, bd, rH)
    face3(p(0f,0f,bh), p(bw,0f,bh), rf, IsoFront.copy(alpha = 0.85f))
    face4(p(bw,0f,bh), p(bw,bd,bh), rb, rf, IsoSide.copy(alpha = 0.80f))
    face4(p(0f,0f,bh), rf, rb, p(0f,bd,bh), IsoTop.copy(alpha = 0.90f))

    // İki baca
    fun chimney(rOff: Float, lOff: Float) {
        val baseH = bh + (rH - bh) * 0.35f; val ch = 0.16f; val cw = 0.08f; val cd = 0.07f
        face4(p(rOff,lOff,baseH+ch), p(rOff+cw,lOff,baseH+ch),
              p(rOff+cw,lOff+cd,baseH+ch), p(rOff,lOff+cd,baseH+ch), IsoTop)
        face4(p(rOff,lOff,baseH), p(rOff+cw,lOff,baseH),
              p(rOff+cw,lOff,baseH+ch), p(rOff,lOff,baseH+ch), IsoFront)
        face4(p(rOff+cw,lOff,baseH), p(rOff+cw,lOff+cd,baseH),
              p(rOff+cw,lOff+cd,baseH+ch), p(rOff+cw,lOff,baseH+ch), IsoSide)
    }
    chimney(0.22f, 0.12f)
    chimney(0.54f, 0.08f)

    // İki pencere (ön)
    val wW = 0.15f; val wH = 0.13f; val wB = bh * 0.34f
    face4(p(0.14f,0f,wB), p(0.14f+wW,0f,wB), p(0.14f+wW,0f,wB+wH), p(0.14f,0f,wB+wH), IsoOpen)
    face4(p(0.68f,0f,wB), p(0.68f+wW,0f,wB), p(0.68f+wW,0f,wB+wH), p(0.68f,0f,wB+wH), IsoOpen)

    // Kapı (orta)
    val dW = 0.17f; val dH = 0.27f; val dR = (bw - dW) * 0.5f
    face4(p(dR,0f,0f), p(dR+dW,0f,0f), p(dR+dW,0f,dH), p(dR,0f,dH), IsoOpen)
}

// ─── Dışa açık composable ─────────────────────────────────────────────────────

@Composable
fun DomainIcon(domain: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        when (domain) {
            "evim"    -> drawHouse()
            "aracim"  -> drawCar()
            "seyahat" -> drawSuitcase()
            "saglik"  -> drawMedKit()
            "ailem"   -> drawFamilyHome()
        }
    }
}
