package com.fredy.askquestions.features.ui.screens.contactScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


data class Contact(val name: String)

// fake DB
val contactsDB = listOf(
    Contact("Alexandros Atkins"),
    Contact("Lilli Duran"),
    Contact("Zac Li"),
    Contact("Doris Adkins"),
    Contact("Amaya Durham"),
    Contact("Tillie Jackson"),
    Contact("Declan Wang"),
    Contact("Jared Porter"),
    Contact("Jamal Washington"),
    Contact("Shania Shepherd"),
    Contact("Cindy Fitzgerald"),
    Contact("Anastasia Santiago"),
    Contact("Richard Graham"),
    Contact("Lucinda Ramirez"),
    Contact("Matteo Todd"),
    Contact("Warren Mejia"),
    Contact("Tanisha Garrett"),
    Contact("Yasmin Vasquez"),
    Contact("Kristian Burch"),
    Contact("Willow Valencia"),
    Contact("Ibrahim Rowe"),
    Contact("Lilly Mills"),
    Contact("Sabrina Mora"),
    Contact("Morgan Sears"),
    Contact("Linda Moody"),
    Contact("Rahim Koch"),
    Contact("Conrad Byrd"),
    Contact("Ammar Lawson"),
    Contact("Marilyn Mcmillan"),
    Contact("Briony Booker"),
    Contact("Maisie Monroe"),
    Contact("Kieron Rios"),
    Contact("Mediha Sparks"),
    Contact("Abdullah Lutero"),
    Contact("Lloyd Hendricks"),
    Contact("Georgia Combs"),
    Contact("Monty Webster"),
    Contact("Luisa Cervantes"),
    Contact("Bronte Valenzuela"),
    Contact("Elle Boyer"),
    Contact("Wilson Tyler"),
    Contact("Wayne Rose"),
    Contact("Rita Mclaughlin"),
    Contact("Fiona Schneider"),
    Contact("Alfie Lozano"),
    Contact("Hamza Downs"),
    Contact("Elissa Clayton"),
    Contact("Zohar Mendez"),
    Contact("Sadia Harrington"),
    Contact("Martin Griffith"),
    Contact("Danial Dillon"),
    Contact("Amelie Dennis"),
    Contact("Halima Hooper"),
    Contact("Chanelle Reid"),
    Contact("Ayesha Holloway"),
    Contact("Anusha Knowles"),
    Contact("Anas Jacobson"),
    Contact("Emma Blevins"),
    Contact("Linda Green"),
    Contact("Marie Connor"),
    Contact("Rico Mcintyre"),
)

val grouped = contactsDB.sortedBy { it.name[0] }.groupBy { it.name[0] }

// phone directory comp
@Composable
fun ContactScreen() {
    // pass list state
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box {
        SearchContact(grouped = grouped)

        Row(
            modifier = Modifier.fillMaxSize().padding(top = 60.dp)
        ) {
            ContactList(grouped = grouped, modifier = Modifier.weight(1f), listState)

            AlphabetBar(grouped = grouped) { pos ->
                coroutineScope.launch {
                    listState.animateScrollToItem(index = pos)
                }
            }
        }
    }


}

// search comp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContact(grouped: Map<Char, List<Contact>>) {
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    // search bar
    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = text,
        onQueryChange = { text = it },
        onSearch = { active = false },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = { Text("Hinted search text") },
    ) {
        val flat = grouped.values.flatten().filter { it.name.contains(text, ignoreCase = true) }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            content = {
                item { Text(text = "${flat.size} contacts found") }

                items(items = flat) { contact ->
                    ContactListItem(contact = contact)
                }
            })
    }
}

// bar comp
@Composable
fun AlphabetBar(
    grouped: Map<Char, List<Contact>>,
    toHeader: (Int) -> Unit
) {
    val letterPos = mutableMapOf<Char, Int>()
    var count by rememberSaveable{ mutableIntStateOf(0) }

    // idk, error???
    // a : position first element "0" not work  ->  0  +0 works
    // b : position first element "10" not work -> 11  +1 works
    // c : position first element "12" not work -> 14  +2 works
    // d : position first element "15" not work -> 18  +3 works
    // e : position first element "18" not work -> 22  +4 works
    // f : position first element "10" not work -> 26  +5 works

    grouped.forEach { (letter, contactsForLetter) ->
        letterPos[letter] = count
        //count += contactsForLetter.size
        count += (contactsForLetter.size+1)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 6.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ){
        for (alphabet in 'A'..'Z') {
            Box(
                modifier = Modifier
                    .clickable { letterPos[alphabet]?.let { toHeader(it) } }
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = alphabet.toString(), textAlign = TextAlign.Center)
            }
        }
    }
}

// contact list comp
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactList(
    grouped: Map<Char, List<Contact>>,
    modifier: Modifier,
    listState: LazyListState
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 18.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        state = listState
    ){
        grouped.forEach { (letter, contactForLetter) ->
            // experimental sticky
            stickyHeader {
                ContactListHeader(initial = letter)
            }

            items(items = contactForLetter) { contact ->
                ContactListItem(contact = contact)
                if (contactForLetter.indexOf(contact) != contactForLetter.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 42.dp, top = 9.dp),
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// header comp
@Composable
fun ContactListHeader(initial: Char) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = initial.toString(),
            color = Color.DarkGray
        )
    }
}

// items comp
@Composable
fun ContactListItem(contact: Contact) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0xFF957fef))
                .size(36.dp),
            contentAlignment = Alignment.Center
        ){
            Text(text = contact.name[0].toString(), color = Color.White)
        }

        Text(text = contact.name, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    }
}