package com.nyansapoai.teaching.domain.models.assessments.literacy

import kotlinx.serialization.Serializable

@Serializable
data class MultipleChoices(
    val correctChoices: List<String>,
    val wrongChoices: List<String>
)

@Serializable
data class QuestionData(
    val question: String,
    val multipleChoices: MultipleChoices
)

@Serializable
data class StoryData(
    val story: String,
    val questionsData: List<QuestionData>
)

@Serializable
data class LiteracyAssessmentData(
    val letters: List<String>,
    val words: List<String>,
    val paragraphs: List<String>,
    val storys: List<String>,
    val questionsData: List<QuestionData>

)


val literacyAssessmentContent = listOf(
    // Grade 3
    LiteracyAssessmentData(
        letters = listOf("i", "j", "k", "m", "x", "r", "b", "c", "d"),
        words = listOf("book", "coat", "sun", "desk", "fish", "ball", "tea", "rat", "house"),
        paragraphs = listOf(
            "Nakuru is a big town. It has a busy market. There are many shops. Many people live there.",
            "Our home is near a forest. The forest has many trees. Some trees are tall. They give us shade."
        ),
        storys = listOf(
            "Janet had a birthday party. The party was at her home. I went there with my brother. There were other boys and girls. Janet came out to meet us. Her mother was happy with us. She told all of us to sit down. Some girls in red hats gave us food. There was rice, beans, meat and bananas. They also gave us sweets and juice. The birthday cake was white and pink. It was very sweet to eat. We sang and danced for two hours. We enjoyed the party very much."
        ),
        questionsData = listOf(
            QuestionData(
                question = "Whose birthday party was it?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "Janet had a birthday Party.",
                        " It was Janet's birthday Party."
                    ),
                    wrongChoices = listOf(
                        "My birthday party.",
                        "Jacob's birthday party.",
                        "The girl's birthday party.",
                        "Our birthday party."
                    )
                )
            ),
            QuestionData(
                question = "How can we tell that the people were happy?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "We enjoyed the party very much. ",
                        "They enjoyed the party very much. "
                    ),
                    wrongChoices = listOf(
                        "We sanged and danced for two hours.",
                        "The cake was sweet to eat.",
                        "They gave us sweets and juice.",
                        "The birthday cake was white and pink."
                    )
                )
            )
        )
    ),

    // Grade 4
    LiteracyAssessmentData(
        letters = listOf("d", "x", "w", "r", "i", "j", "m", "n", "w"),
        words = listOf("soap", "mango", "star", "face", "body", "puppy", "finger", "seed", "eye"),
        paragraphs = listOf(
            "We live in a hut. It is very big. It has four windows. Our mother built it last year.",
            "This is my elder sister. She is a nurse. She works in the big hospital. She treats sick people."
        ),
        storys = listOf(
            "Our school opened on fifth May. Many children came to school early. They were all happy to be back. Peter and Jim were also there. The two boys are my good friends. Peter told us he visited his aunt. She bought him a black pair of shoes. Jim wanted to wear the new shoes. Peter told him that his feet were dirty. Jim got angry and shouted at Peter. Some children came to look at them. They made a lot of noise. The teacher came from the office. She told Peter and Jim not to fight."
        ),
        questionsData = listOf(
            QuestionData(
                question = "When did our school open?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " On fifth May. ",
                        " Our school opened on fifth May. "
                    ),
                    wrongChoices = listOf(
                        " Our school opened today. ",
                        "Our school opened on third May. ",
                        " Our school opened on second May. ",
                        " Our school opened on first May. "
                    )
                )
            ),
            QuestionData(
                question = "What did the teacher do when she came from the office?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "She told Peter and Jim not to fight. ",
                        " The teacher told Peter and Jim not to fight. "
                    ),
                    wrongChoices = listOf(
                        "The teacher caned Peter and Jim. ",
                        "The teacher took Peter's shoes. ",
                        "The teacher told Peter and Jim to go home. ",
                        " The teacher told Peter and Jim to go to her office. "
                    )
                )
            )
        )
    ),

    // Grade 5
    LiteracyAssessmentData(
        letters = listOf("r", "b", "c", "j", "m", "i", "k","x", "u"),
        words = listOf("bag", "mango", "dad", "crops", "bush", "room", "pot", "hen", "fish", "hand"),
        paragraphs = listOf(
            "Our flag has four colors. We learn about them in school. They tell us about our country. We love our flag very much.",
            "Kibet lives in Molo. He is a farmer. He grows maize and beans. He also has many cows"
        ),
        storys = listOf(
            "A long time ago there was a cow. She lived in a big forest. The forest had many wild animals. Some animals used to kill and eat others. One day the cow gave birth to a calf. She loved it very much. She did not want the animals to eat it. One morning the cow went to the lion. She wanted him to help her. The lion roared at them. The cow and her calf ran away. They found a man outside his house. The man loved the animals. He made a cow shed for them. The cow never went back to the forest."
        ),
        questionsData = listOf(
            QuestionData(
                question = "What did the forest have?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " The forest had many wild animals. ",
                        " Many wild animals. "
                    ),
                    wrongChoices = listOf(
                        "The forest had many children. ",
                        " The forest had many cars. ",
                        "The forest had big roads. ",
                        "The forest had many balls. "
                    )
                )
            ),
            QuestionData(
                question = "Why did the cow and her calf run away?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "The lion roared at the cow and her calf. ",
                        "The lion roared at them. "
                    ),
                    wrongChoices = listOf(
                        "The lion laughed at them. ",
                        "The lion laughed at the lion and her calf. ",
                        "The lion run after the lion and her calf. ",
                        "The cow gave birth to a calf. "
                    )
                )
            )
        )
    ),

    // Grade 6
    LiteracyAssessmentData(
        letters = listOf("d", "j", "p", "e", "r", "u", "h", "f", "c", "s"),
        words = listOf("egg", "sun", "duck", "bush", "mat", "food", "doll", "pig", "vest", "sheep"),
        paragraphs = listOf(
            "Musa is a brother to Maria. He is in class two. His sister is in class three. They are good friends.",
            "My father has built a beautiful house. It is very big. We like it a lot. We will move in today."
        ),
        storys = listOf(
            "Ali lives near Lake Turkana. He has a wife and four children. All his children go to school. He works very hard to keep them in school. He gives them all they need. The children work hard in school. Ali has a boat and many fishing nets. Everyday he goes fishing in the lake. He wakes up very early to set his nets. His friend James goes with him. The two catch a lot of fish. They sell most of the fish in the market. They then take the rest to their homes. Their families like the fish very much."
        ),
        questionsData = listOf(
            QuestionData(
                question = "How many children does Ali have?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "He has four children. ",
                        " Ali hasfour children. "
                    ),
                    wrongChoices = listOf(
                        "Ali has a wife. ",
                        "Ali has three children. ",
                        "Ali has two children. ",
                        "Ali has five children. "
                    )
                )
            ),
            QuestionData(
                question = "What do we learn about Lake Turkana from the story?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " Lake Turkana has a lot of fish. ",
                        " The Lake has fish. "
                    ),
                    wrongChoices = listOf(
                        "The Lake Turkana has a lot of cows. ",
                        "The Lake Turkana has a lot of birds. ",
                        "The Lake Turkana has a lot of goats. ",
                        "The Lake Turkana has a lot of dogs. "
                    )
                )
            )
        )
    ),

    // Grade 7
    LiteracyAssessmentData(
        letters = listOf("c", "u", "v", "j", "g", "b", "z", "t", "o", "w"),
        words = listOf("sock", "milk", "dad", "pin", "book", "home", "ear", "bag", "seven", "lion"),
        paragraphs = listOf(
            "My mother is a nurse. She works in a big hospital. She looks after sick people. She likes her work.",
            "Last week we visited a farm. There were many workers. We saw many crops. We will plant some in school."
        ),
        storys = listOf(
            "Long time ago hare and hyena were friends. They lived in the same village. The hare was a good dancer. He always called hyena to come see him dance. The hyena liked the way hare danced. He tried the dance at his home. He became a good dancer too. One day all the animals went for a dance. They wanted to see who was the best dancer. They all danced very well. Hare and hyena danced better than the rest. Hare knew he would win. The animals were asked to name the winner. Most of them clapped for the hyena. The hare was very sad."
        ),
        questionsData = listOf(
            QuestionData(
                question = "Why did the hare always invite the hyena to his home?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " He always called Hyena to come see him dance.",
                        " The Hare always invited the hyena to his home to come see him dance."
                    ),
                    wrongChoices = listOf(
                        " He invited the hyena to come cook with him. ",
                        " He invited the hyena to come eat with him. ",
                        " The Hare invited the hyena to his house to talk. ",
                        " The Hare invited the hyena to his house to learn. "
                    )
                )
            ),
            QuestionData(
                question = "Why was the hare not happy after the dance?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " Most of the animals clapped for the hyena. ",
                        " The hare was not the winner."
                    ),
                    wrongChoices = listOf(
                        "The hare was the winner. ",
                        " Most of the animals clapped for the hare. ",
                        " The hare broke his foot. ",
                        " The hare was tired. ",
                        "The hare lost his book."
                    )
                )
            )
        )
    ),

    // Grade 8
    LiteracyAssessmentData(
        letters = listOf("i", "d", "p", "c", "f", "k", "m", "z", "a", "r"),
        words = listOf("boy", "dock", "ship", "leg", "foot", "ball", "rat", "jug", "class", "face"),
        paragraphs = listOf(
            "Our school has many trees. There is one big mango tree. The tree is behind the office. It gives us many fruits",
            "We have a dog at home. She has a puppy. She gave birth to it yesterday. I named the puppy Rambo."
        ),
        storys = listOf(
            "Long ago there lived a hyena and a dog. They lived in the middle of the forest. The two were close friends. The dog was very hardworking and brave. He planted all types of fruits. Hyena on the other hand was lazy. He kept stealing the fruits from the farm. The dog was very unhappy one day. He decided to lay a trap. He wanted to teach the thief a lesson. He dug a big hole in his farm. He covered the hole with leaves. Hyena came to steal and fell in the hole. He broke two of his legs. That is why hyena limps to this day."
        ),
        questionsData = listOf(
            QuestionData(
                question = "Where did the hyena and the dog live?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "They lived in the middle of the forest. ",
                        "The hyena and the dog lived in the forest."
                    ),
                    wrongChoices = listOf(
                        " They lived in a big house. ",
                        "They lived on the streets. ",
                        " The hyena and the dog lived in a farm. ",
                        "The hyena and the dog lived in the market. "
                    )
                )
            ),
            QuestionData(
                question = "Why did the hyena fall into the hole?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "The hyena wanted to steal from the dog. ",
                        " The hyena went to the dog's farm. "
                    ),
                    wrongChoices = listOf(
                        "The hyena went to the dog's house. ",
                        " The hyena went to the dog's school. ",
                        "The hyena was hardworking and brave. ",
                        "The hyena broke two of his legs. "
                    )
                )
            )
        )
    ),

    // Grade 9
    LiteracyAssessmentData(
        letters = listOf("e", "x", "d", "w", "k", "c", "h", "b", "j", "a"),
        words = listOf("room", "face", "table", "dog", "desk", "pen", "ear", "fish", "bean", "man"),
        paragraphs = listOf(
            "My mother works in Lamu. Lamu is a busy town. The people there are good. They are very kind.",
            "Our village shop is big. It is near the road. Omari is the shopkeeper. He sells sugar and milk."
        ),
        storys = listOf(
            "Juma reads to us a story from his book everyday. He reads the story aloud in class. We enjoy listening to the stories. Yesterday he read about the sun and the wind. Both of them lived in the same sky. The wind did not like the sun. It wanted to be the head of the sky. One day the wind chased the sun away. It told the sun to go to another sky. The sun did not go. The next morning the wind ran after the sun. The sun fell down and started crying. That is how it began to rain. We clapped for Juma."
        ),
        questionsData = listOf(
            QuestionData(
                question = "What does Juma do every day?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "Juma reads to us a story from his book. ",
                        " He reads to us a story from his book every day. "
                    ),
                    wrongChoices = listOf(
                        "Juma cooks for us every day. ",
                        "Juma goes to play every day. ",
                        "Juma plays games with us every day. ",
                        " He runs everyday. "
                    )
                )
            ),
            QuestionData(
                question = "How did the rain begin?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "The sun fell down and started crying. ",
                        " The sun started crying. "
                    ),
                    wrongChoices = listOf(
                        " The sky fell down and started crying. ",
                        "The wind was crying. ",
                        " Juma fell down and started crying. ",
                        "The clouds started crying. "
                    )
                )
            )
        )
    ),

    // Grade 10
    LiteracyAssessmentData(
        letters = listOf("j", "z", "b", "h", "o", "d", "w", "r", "e", "m"),
        words = listOf("body", "ant", "book", "house", "peg", "sand", "milk", "car", "pot", "shoe"),
        paragraphs = listOf(
            "Atieno is my sister. She is in class four. She has a blue skirt. She likes it very much.",
            "Our school is in town. It has a big farm. We plant maize and beans. We also keep chicken."
        ),
        storys = listOf(
            "There was little rain last year. Many rivers are now dry. There are no crops on the farms. All plants have dried up. People do not have enough food. The animals do not have water to drink. The farmers are waiting for the rain to fall. Maria is a farmer in our village. When it rains she will plant vegetables. She will also plant maize, beans, and sweet potatoes. She will sell the maize at the market. Maria will plant one hundred and eighty trees. Maria likes trees because they give fresh air. Trees also give shade and bring more rain."
        ),
        questionsData = listOf(
            QuestionData(
                question = "What are farmers waiting for?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " The farmers are waiting for the rain to fall. ",
                        "The farmers are waiting for the rain. "
                    ),
                    wrongChoices = listOf(
                        "The farmers are waiting for the wind. ",
                        " The farmers are waiting for the plants to grow. ",
                        " The farmers are waiting for the sun. ",
                        " The farmers are waiting for their money. "
                    )
                )
            ),
            QuestionData(
                question = "Why does Maria want to plant trees?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        " Maria likes trees because they give fresh air. ",
                        " She likes trees because they give fresh air. "
                    ),
                    wrongChoices = listOf(
                        " Maria wants to plant trees because she likes books. ",
                        " Maria wants to plant trees because she likes birds. ",
                        " Maria wants to plant trees because she wants to sell maize at the market. ",
                        " Maria wants to plant trees because she wants to sell them at the market. "
                    )
                )
            )
        )
    ),

    //Dignitas
    LiteracyAssessmentData(
        letters = listOf("a, 'f", "s", "b", "y"),
        words = listOf("bag", "rat", "nose", "rope", "dress"),
        paragraphs = listOf(
            "Mary and Ali were playing \n" +
                    "a game. They were sitting \n" +
                    "at a small table. Pipo, their \n" +
                    "pet dog, was lying on the \n" +
                    "floor next to them. Mary \n" +
                    "won the game and \n" +
                    "jumped up and down with \n" +
                    "joy. Pipo barked in pain \n" +
                    "and Mary felt bad.",
        ),
        storys = listOf(
            "Tina was playing in the garden. She found a little box by a tree. Tina\n" +
                    "picked it up and opened it. The box was filled with colourful pencils.\n" +
                    "She took the box home and showed it to her mother. Tina’s mother told\n" +
                    "her, \"Tina, you should try and find the owner of the box”. Tina went\n" +
                    "back to the garden. She saw a boy standing by the tree. He looked\n" +
                    "sad. Tina showed him the box. The boy’s face lit up with a smile."
        ),
        questionsData = listOf(
            QuestionData(
                question = "Where was Tina playing?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "",
                        ""
                    ),
                    wrongChoices = listOf(
                        "",
                        "",
                        "",
                        ""
                    )
                )
            ),
            QuestionData(
                question = "What did Tina find?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "",
                        ""
                    ),
                    wrongChoices = listOf(
                        "",
                        "",
                        "",
                        ""
                    )
                )
            ),
            QuestionData(
                question = "Who was near the tree where Tina found the box?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "",
                        ""
                    ),
                    wrongChoices = listOf(
                        "",
                        "",
                        "",
                        ""
                    )
                )
            ),
            QuestionData(
                question = "Why do you think the boy’s face lit up with a smile?",
                multipleChoices = MultipleChoices(
                    correctChoices = listOf(
                        "",
                        ""
                    ),
                    wrongChoices = listOf(
                        "",
                        "",
                        "",
                        ""
                    )
                )
            )
        )
    )
)