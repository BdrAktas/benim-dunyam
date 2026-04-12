package com.simay.lifebank.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simay.lifebank.ui.components.AnimatedProgress
import com.simay.lifebank.ui.components.DomainHeader
import com.simay.lifebank.ui.components.GlassButton
import com.simay.lifebank.ui.components.GlassIntensity
import com.simay.lifebank.ui.components.GlassSurface
import com.simay.lifebank.ui.components.GlassTabs
import com.simay.lifebank.ui.components.ProactiveOffer
import com.simay.lifebank.ui.components.ProductCard
import com.simay.lifebank.ui.components.TabItem
import com.simay.lifebank.ui.components.animateCountUp
import com.simay.lifebank.ui.theme.Bark
import com.simay.lifebank.ui.theme.Honey
import com.simay.lifebank.ui.theme.Lav
import com.simay.lifebank.ui.theme.Moss
import com.simay.lifebank.ui.theme.Pebble
import com.simay.lifebank.ui.theme.Rose
import com.simay.lifebank.ui.theme.SansFont
import com.simay.lifebank.ui.theme.SerifFont
import com.simay.lifebank.ui.theme.Sky
import com.simay.lifebank.ui.theme.Stone
import com.simay.lifebank.ui.util.formatTRY

private data class FamilyMember(
    val name: String,
    val avatar: String,
    val color: Color,
    val spent: Int
)

private data class Expense(
    val title: String,
    val amount: Int,
    val icon: String
)

private data class FamilyGoal(
    val title: String,
    val target: Int,
    val current: Int,
    val icon: String,
    val deadline: String
)

private data class ChildTransaction(
    val title: String,
    val amount: Int,
    val date: String
)

@Composable
fun AilemScreen(onBack: () -> Unit) {
    var tab by remember { mutableStateOf("butce") }
    var allowance by remember { mutableStateOf("500") }
    var editingA by remember { mutableStateOf(false) }
    val familyTotal = animateCountUp(target = 33500, duration = 800)

    val members = remember {
        listOf(
            FamilyMember(name = "Bedir", avatar = "B", color = Sky, spent = 18400),
            FamilyMember(name = "Eş", avatar = "E", color = Rose, spent = 12300),
            FamilyMember(name = "Çocuk", avatar = "Ç", color = Moss, spent = 2800)
        )
    }

    val expenses = remember {
        listOf(
            Expense(title = "Market", amount = 4820, icon = "🛒"),
            Expense(title = "Eğitim", amount = 6500, icon = "📚"),
            Expense(title = "Faturalar", amount = 4180, icon = "🏠"),
            Expense(title = "Ulaşım", amount = 2400, icon = "🚌"),
            Expense(title = "Eğlence", amount = 1920, icon = "🎬")
        )
    }

    val goals = remember {
        listOf(
            FamilyGoal(title = "Yaz Tatili", target = 35000, current = 22000, icon = "🏖️", deadline = "Haz 2026"),
            FamilyGoal(title = "Eğitim Fonu", target = 100000, current = 67000, icon = "🎓", deadline = "2028"),
            FamilyGoal(title = "Ev Tadilatı", target = 50000, current = 8000, icon = "🔨", deadline = "2027")
        )
    }

    val childTxs = remember {
        listOf(
            ChildTransaction(title = "Kitapçı", amount = -120, date = "10 Nis"),
            ChildTransaction(title = "Oyun", amount = -85, date = "8 Nis"),
            ChildTransaction(title = "Harçlık", amount = 500, date = "7 Nis"),
            ChildTransaction(title = "Kantin", amount = -45, date = "7 Nis")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        DomainHeader(label = "Ailem", subtitle = "Aile Finansları", onBack = onBack)

        // Tabs
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            GlassTabs(
                tabs = listOf(
                    TabItem(id = "butce", label = "Bütçe"),
                    TabItem(id = "hedef", label = "Hedefler"),
                    TabItem(id = "cocuk", label = "Çocuk")
                ),
                activeId = tab,
                onTabChange = { tab = it }
            )
        }

        // Content
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            when (tab) {
                "butce" -> ButceTab(members, expenses, familyTotal)
                "hedef" -> HedeflerTab(goals)
                "cocuk" -> CocukTab(
                    childTxs = childTxs,
                    allowance = allowance,
                    onAllowanceChange = { allowance = it },
                    editingA = editingA,
                    onEditingChange = { editingA = it }
                )
            }
        }
    }
}

@Composable
private fun ButceTab(
    members: List<FamilyMember>,
    expenses: List<Expense>,
    familyTotal: Int
) {
    val totalSpent = 33500

    // Summary card
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Strong,
        contentPadding = PaddingValues(18.dp),
        modifier = Modifier.padding(bottom = 14.dp)
    ) {
        Text(
            text = "NİSAN AİLE GİDERİ",
            fontSize = 10.sp,
            color = Pebble,
            fontFamily = SansFont,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = formatTRY(familyTotal),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = Bark,
            fontFamily = SerifFont,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Stacked bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            members.forEach { m ->
                Box(
                    modifier = Modifier
                        .weight(m.spent.toFloat() / totalSpent.toFloat())
                        .height(8.dp)
                        .background(m.color)
                )
            }
        }

        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            members.forEach { m ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(m.color, CircleShape)
                    )
                    Text(
                        text = "${m.name}: ${formatTRY(m.spent)}",
                        fontSize = 10.sp,
                        color = Stone,
                        fontFamily = SansFont
                    )
                }
            }
        }
    }

    // Expense categories
    expenses.forEach { e ->
        GlassSurface(
            animate = true,
            intensity = GlassIntensity.Subtle,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = e.icon, fontSize = 16.sp)
                    Text(
                        text = e.title,
                        fontSize = 13.sp,
                        color = Bark,
                        fontFamily = SansFont
                    )
                }
                Text(
                    text = formatTRY(e.amount),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Bark,
                    fontFamily = SansFont
                )
            }
        }
    }
}

@Composable
private fun HedeflerTab(goals: List<FamilyGoal>) {
    goals.forEachIndexed { i, g ->
        val pct = (g.current.toFloat() / g.target.toFloat() * 100f).toInt()

        GlassSurface(
            animate = true,
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = g.icon, fontSize = 22.sp)
                    Column {
                        Text(
                            text = g.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Bark,
                            fontFamily = SansFont
                        )
                        Text(
                            text = g.deadline,
                            fontSize = 11.sp,
                            color = Stone,
                            fontFamily = SansFont
                        )
                    }
                }
                Text(
                    text = "%$pct",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Honey,
                    fontFamily = SerifFont
                )
            }

            // Progress bar
            AnimatedProgress(
                value = g.current.toFloat(),
                max = g.target.toFloat(),
                color = if (pct > 80) Moss else Honey,
                height = 8.dp,
                delayMs = i * 200
            )

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTRY(g.current),
                    fontSize = 11.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
                Text(
                    text = "Kalan: ${formatTRY(g.target - g.current)}",
                    fontSize = 11.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
            }
        }
    }

    ProactiveOffer(
        emoji = "🎓",
        title = "Eğitim Kredisi",
        limit = 100000,
        rate = "%2.49",
        term = "48 aya kadar",
        desc = "Çocuğunuzun eğitim fonunu hızlandırın. Özel okul ve üniversite için uygun faiz.",
        cta = "Başvur",
        color = Lav,
        highlight = true,
        socialProof = "4.600 aile eğitim kredisi kullanıyor",
        savingsVs = "Eğitim fonunuzu 2 yıl erkene çekin",
        authority = "Ön Onaylı"
    )

    ProductCard(
        emoji = "🐣",
        title = "İlk Param Birikim",
        desc = "Çocuğunuz için düzenli birikim.",
        cta = "Hesap Aç",
        color = Moss
    )
}

@Composable
private fun CocukTab(
    childTxs: List<ChildTransaction>,
    allowance: String,
    onAllowanceChange: (String) -> Unit,
    editingA: Boolean,
    onEditingChange: (Boolean) -> Unit
) {
    // Child account card
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Strong,
        accent = Moss,
        glow = true,
        contentPadding = PaddingValues(18.dp),
        modifier = Modifier.padding(bottom = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Çocuk Hesabı",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Bark,
                    fontFamily = SansFont
                )
                Text(
                    text = formatTRY(3240),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium,
                    color = Moss,
                    fontFamily = SerifFont,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            // Avatar circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Moss, Sky)
                        ),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = "Ç",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    fontFamily = SansFont
                )
            }
        }
    }

    // Allowance card
    GlassSurface(
        animate = true,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.padding(bottom = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Haftalık Harçlık",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Bark,
                    fontFamily = SansFont
                )
                Text(
                    text = "Her Pazartesi · Otomatik",
                    fontSize = 11.sp,
                    color = Stone,
                    fontFamily = SansFont
                )
            }
            if (editingA) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TextField(
                        value = allowance,
                        onValueChange = onAllowanceChange,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SansFont,
                            textAlign = TextAlign.Center
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.5f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.5f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .width(60.dp)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Color.White.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            ),
                        singleLine = true
                    )
                    GlassButton(
                        text = "✓",
                        color = Moss,
                        isSmall = true,
                        onClick = { onEditingChange(false) }
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = formatTRY(allowance.toIntOrNull() ?: 500),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Bark,
                        fontFamily = SerifFont
                    )
                    GlassButton(
                        text = "Düzenle",
                        color = Pebble,
                        isSmall = true,
                        isOutline = true,
                        onClick = { onEditingChange(true) }
                    )
                }
            }
        }
    }

    // Recent transactions header
    Text(
        text = "Son Harcamalar",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Bark,
        fontFamily = SerifFont,
        modifier = Modifier.padding(bottom = 10.dp)
    )

    // Transaction list
    childTxs.forEach { tx ->
        GlassSurface(
            animate = true,
            intensity = GlassIntensity.Subtle,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = tx.title,
                        fontSize = 13.sp,
                        color = Bark,
                        fontFamily = SansFont
                    )
                    Text(
                        text = tx.date,
                        fontSize = 10.sp,
                        color = Pebble,
                        fontFamily = SansFont
                    )
                }
                Text(
                    text = "${if (tx.amount > 0) "+" else ""}${formatTRY(tx.amount)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tx.amount > 0) Moss else Bark,
                    fontFamily = SansFont
                )
            }
        }
    }

    // Quick send section
    GlassSurface(
        animate = true,
        intensity = GlassIntensity.Subtle,
        contentPadding = PaddingValues(14.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(
            text = "Hızlı Gönder",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Bark,
            fontFamily = SansFont,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(100, 250, 500).forEach { v ->
                Box(modifier = Modifier.weight(1f)) {
                    GlassButton(
                        text = "₺$v",
                        color = Honey,
                        isSmall = true,
                        isOutline = true,
                        isFull = true
                    )
                }
            }
        }
    }
}
