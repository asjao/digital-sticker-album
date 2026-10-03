package com.example.stickeralbum.data

data class QuizQuestion(
    val question: String,
    val answers: List<String>,
    val correctAnswerIndex: Int
)

val quizQuestions = listOf(

    QuizQuestion(
        question = "Which country won the 2022 FIFA World Cup?",
        answers = listOf("France", "Argentina", "Brazil", "Germany"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country won the 2018 FIFA World Cup?",
        answers = listOf("France", "Croatia", "Germany", "Argentina"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country won the 2014 FIFA World Cup?",
        answers = listOf("Argentina", "Brazil", "Germany", "Spain"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country won the 2010 FIFA World Cup?",
        answers = listOf("Spain", "Netherlands", "Germany", "Italy"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country won the 2006 FIFA World Cup?",
        answers = listOf("France", "Brazil", "Italy", "Germany"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country won the 2002 FIFA World Cup?",
        answers = listOf("Germany", "Brazil", "France", "Argentina"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country has won the most FIFA World Cups?",
        answers = listOf("Germany", "Argentina", "Italy", "Brazil"),
        correctAnswerIndex = 3
    ),

    QuizQuestion(
        question = "Which country hosted the first FIFA World Cup?",
        answers = listOf("Brazil", "Uruguay", "Italy", "France"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "In which year was the first FIFA World Cup held?",
        answers = listOf("1926", "1930", "1934", "1938"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country hosted the 2022 FIFA World Cup?",
        answers = listOf("Qatar", "Brazil", "Russia", "United Arab Emirates"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which three countries host the 2026 FIFA World Cup?",
        answers = listOf(
            "USA, Canada and Mexico",
            "Spain, Portugal and France",
            "Brazil, Argentina and Uruguay",
            "Germany, France and Italy"
        ),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which African country reached the World Cup semi-finals for the first time in 2022?",
        answers = listOf("Egypt", "Morocco", "Senegal", "Ghana"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Who won the Golden Boot at the 2022 FIFA World Cup?",
        answers = listOf("Lionel Messi", "Kylian Mbappé", "Olivier Giroud", "Julián Álvarez"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Who won the Golden Ball at the 2022 FIFA World Cup?",
        answers = listOf("Lionel Messi", "Kylian Mbappé", "Luka Modrić", "Emiliano Martínez"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country did Argentina defeat in the 2022 World Cup final?",
        answers = listOf("Croatia", "Netherlands", "France", "Brazil"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Who captained Argentina to the 2022 World Cup title?",
        answers = listOf("Ángel Di María", "Lionel Messi", "Lautaro Martínez", "Emiliano Martínez"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country lost to France in the 2018 World Cup final?",
        answers = listOf("Belgium", "England", "Croatia", "Germany"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country did Germany defeat in the 2014 World Cup final?",
        answers = listOf("Brazil", "Argentina", "Netherlands", "Spain"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country did Spain defeat in the 2010 World Cup final?",
        answers = listOf("Germany", "Brazil", "Netherlands", "Argentina"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country did Italy defeat in the 2006 World Cup final?",
        answers = listOf("Germany", "France", "Brazil", "Portugal"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country did Brazil defeat in the 2002 World Cup final?",
        answers = listOf("Germany", "France", "Italy", "Argentina"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country won the 1998 FIFA World Cup?",
        answers = listOf("Brazil", "Germany", "France", "Italy"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country won the 1994 FIFA World Cup?",
        answers = listOf("Brazil", "Italy", "Germany", "Argentina"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country won the 1986 FIFA World Cup?",
        answers = listOf("Germany", "Brazil", "Argentina", "Italy"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country won the 1970 FIFA World Cup?",
        answers = listOf("Brazil", "Italy", "Germany", "Argentina"),
        correctAnswerIndex = 0
    ),


    QuizQuestion(
        question = "Which player scored the famous 'Hand of God' goal?",
        answers = listOf("Pelé", "Diego Maradona", "Johan Cruyff", "Zinedine Zidane"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country has appeared in every FIFA World Cup tournament?",
        answers = listOf("Germany", "Argentina", "Brazil", "Italy"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Who scored the winning goal in the 2010 World Cup final?",
        answers = listOf("David Villa", "Xavi", "Andrés Iniesta", "Fernando Torres"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which player scored a hat-trick in the 2022 World Cup final?",
        answers = listOf("Lionel Messi", "Kylian Mbappé", "Olivier Giroud", "Ángel Di María"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which team did Croatia defeat in the 2018 World Cup semi-final?",
        answers = listOf("England", "France", "Belgium", "Russia"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which team did Morocco defeat in the 2022 World Cup quarter-final?",
        answers = listOf("Spain", "Portugal", "Brazil", "Belgium"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country won the 1958 FIFA World Cup?",
        answers = listOf("Brazil", "Sweden", "West Germany", "France"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which legendary Brazilian player won three FIFA World Cups?",
        answers = listOf("Ronaldo", "Ronaldinho", "Pelé", "Romário"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Who scored the winning goal for Germany in the 2014 World Cup final?",
        answers = listOf("Thomas Müller", "Mario Götze", "Miroslav Klose", "Toni Kroos"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which team did Brazil defeat in the 1970 World Cup final?",
        answers = listOf("Italy", "West Germany", "Argentina", "Netherlands"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "How many players does one football team normally have on the field?",
        answers = listOf("9", "10", "11", "12"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which player is allowed to use their hands inside their own penalty area?",
        answers = listOf("Captain", "Goalkeeper", "Defender", "Any player"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "How far is the penalty spot from the goal line?",
        answers = listOf("9 metres", "10 metres", "11 metres", "12 metres"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "How long is a normal football match, excluding added time?",
        answers = listOf("80 minutes", "90 minutes", "100 minutes", "120 minutes"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "What happens when a player receives a red card?",
        answers = listOf(
            "The player must leave the field",
            "The player misses five minutes",
            "The team receives a penalty kick",
            "The match immediately ends"
        ),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "How often is the FIFA World Cup normally held?",
        answers = listOf("Every 2 years", "Every 3 years", "Every 4 years", "Every 5 years"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "How many teams participate in the 2026 FIFA World Cup?",
        answers = listOf("32", "36", "40", "48"),
        correctAnswerIndex = 3
    ),

    QuizQuestion(
        question = "Which country won the first World Cup to be held in Europe?",
        answers = listOf("Italy", "France", "Germany", "Sweden"),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which player holds the record for the most goals in FIFA World Cup history?",
        answers = listOf("Pelé", "Ronaldo", "Miroslav Klose", "Lionel Messi"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which country reached the 1954 World Cup final but lost to West Germany?",
        answers = listOf("Brazil", "Hungary", "Austria", "Uruguay"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which two countries played in the 2018 FIFA World Cup final?",
        answers = listOf(
            "France and Croatia",
            "France and Belgium",
            "Croatia and England",
            "Germany and France"
        ),
        correctAnswerIndex = 0
    ),

    QuizQuestion(
        question = "Which country won the 2014 World Cup semi-final 7–1 against Brazil?",
        answers = listOf("Argentina", "Netherlands", "Germany", "Spain"),
        correctAnswerIndex = 2
    ),

    QuizQuestion(
        question = "Which player scored two goals for Brazil in the 2002 World Cup final?",
        answers = listOf("Rivaldo", "Ronaldo", "Ronaldinho", "Cafu"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country finished third at the 2022 FIFA World Cup?",
        answers = listOf("Morocco", "Croatia", "France", "Netherlands"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which team did Argentina defeat in the 2022 World Cup semi-final?",
        answers = listOf("Brazil", "Croatia", "Netherlands", "France"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country hosted the 2014 FIFA World Cup?",
        answers = listOf("South Africa", "Brazil", "Russia", "Germany"),
        correctAnswerIndex = 1
    ),

    QuizQuestion(
        question = "Which country won the 1990 FIFA World Cup?",
        answers = listOf("Argentina", "Italy", "West Germany", "Brazil"),
        correctAnswerIndex = 2
    )
)


fun getRandomQuizQuestions(
    count: Int = 5
): List<QuizQuestion> {
    return quizQuestions
        .shuffled()
        .take(count)
}