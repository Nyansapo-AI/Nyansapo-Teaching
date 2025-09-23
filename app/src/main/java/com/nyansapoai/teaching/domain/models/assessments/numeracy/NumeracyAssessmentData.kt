package com.nyansapoai.teaching.domain.models.assessments.numeracy

import com.nyansapoai.teaching.presentation.assessments.numeracy.components.OperationType

// Kotlin Data Classes for Numeracy Assessment

data class NumeracyOperations(
    val firstNumber: Int,
    val secondNumber: Int,
    val answer: Int,
    val operationType: OperationType = OperationType.ADDITION
)

data class WordProblem(
    val problem: String,
    val answer: Int
)

data class NumeracyAssessmentContent(
    val countAndMatchNumbersList: List<Int>,
    val numberRecognitionList: List<Int>,
    val additions: List<NumeracyOperations>,
    val subtractions: List<NumeracyOperations>,
    val multiplications: List<NumeracyOperations>,
    val divisions: List<NumeracyOperations>,
    val wordProblems: List<WordProblem>
)

data class NumeracyAssessmentData(
    val numeracyAssessmentContentList: List<NumeracyAssessmentContent>
)

// Sample instantiation with the data
val numeracyAssessmentData = NumeracyAssessmentData(
    numeracyAssessmentContentList = listOf(
        // Assessment 1
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(9, 2, 4, 6, 1, 3),
            numberRecognitionList = listOf(74, 66, 28, 39, 97, 45),
            additions = listOf(
                NumeracyOperations(17, 24, 41, OperationType.ADDITION),
                NumeracyOperations(34, 21, 55, OperationType.ADDITION),
                NumeracyOperations(12, 31, 43, OperationType.ADDITION)
            ),
            subtractions = listOf(
                NumeracyOperations(78, 41, 37, OperationType.SUBTRACTION),
                NumeracyOperations(96, 54, 42, OperationType.SUBTRACTION),
                NumeracyOperations(43, 20, 23, OperationType.SUBTRACTION)
            ),
            multiplications = listOf(
                NumeracyOperations(2, 2, 4, OperationType.MULTIPLICATION),
                NumeracyOperations(5, 3, 15, OperationType.MULTIPLICATION),
                NumeracyOperations(3, 4, 12, OperationType.MULTIPLICATION)
            ),
            divisions = listOf(
                NumeracyOperations(20, 5, 4, OperationType.DIVISION),
                NumeracyOperations(12, 2, 6, OperationType.DIVISION),
                NumeracyOperations(6, 3, 2, OperationType.DIVISION)
            ),
            wordProblems = listOf(
                WordProblem(
                    "John has 31 banana's,Peter takes 13 banana's from John.How many banana's does John have left?",
                    18
                )
            )
        ),
        // Assessment 2
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(6, 3, 9, 2, 7, 5),
            numberRecognitionList = listOf(19, 67, 45, 24, 33, 51),
            additions = listOf(
                NumeracyOperations(21, 36, 57),
                NumeracyOperations(15, 34, 49),
                NumeracyOperations(84, 12, 96)
            ),
            subtractions = listOf(
                NumeracyOperations(28, 15, 13),
                NumeracyOperations(33, 11, 22),
                NumeracyOperations(87, 51, 36)
            ),
            multiplications = listOf(
                NumeracyOperations(5, 2, 10),
                NumeracyOperations(2, 3, 6),
                NumeracyOperations(3, 4, 12)
            ),
            divisions = listOf(
                NumeracyOperations(4, 2, 2),
                NumeracyOperations(16, 4, 4),
                NumeracyOperations(10, 5, 2)
            ),
            wordProblems = listOf(
                WordProblem(
                    "Mary has 5 apples. She eats 2 apples. How many apples does Mary have left?",
                    3
                )
            )
        ),
        // Assessment 3
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(8, 4, 1, 3, 6, 7),
            numberRecognitionList = listOf(23, 71, 48, 29, 37, 56),
            additions = listOf(
                NumeracyOperations(25, 39, 64),
                NumeracyOperations(18, 37, 55),
                NumeracyOperations(89, 15, 104)
            ),
            subtractions = listOf(
                NumeracyOperations(32, 17, 15),
                NumeracyOperations(38, 14, 24),
                NumeracyOperations(92, 56, 36)
            ),
            multiplications = listOf(
                NumeracyOperations(6, 3, 18),
                NumeracyOperations(3, 4, 12),
                NumeracyOperations(4, 5, 20)
            ),
            divisions = listOf(
                NumeracyOperations(6, 3, 2),
                NumeracyOperations(20, 5, 4),
                NumeracyOperations(12, 6, 2)
            ),
            wordProblems = listOf(
                WordProblem(
                    "Sarah has 7 oranges. She gives 3 oranges to her friend. How many oranges does Sarah have left?",
                    4
                )
            )
        ),
        // Assessment 4
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(2, 5, 1, 3, 6, 7),
            numberRecognitionList = listOf(24, 73, 49, 31, 38, 57),
            additions = listOf(
                NumeracyOperations(27, 39, 66),
                NumeracyOperations(19, 37, 56),
                NumeracyOperations(88, 14, 102)
            ),
            subtractions = listOf(
                NumeracyOperations(33, 17, 16),
                NumeracyOperations(39, 15, 24),
                NumeracyOperations(91, 52, 39)
            ),
            multiplications = listOf(
                NumeracyOperations(7, 3, 21),
                NumeracyOperations(4, 4, 16),
                NumeracyOperations(5, 5, 25)
            ),
            divisions = listOf(
                NumeracyOperations(8, 4, 2),
                NumeracyOperations(18, 6, 3),
                NumeracyOperations(15, 5, 3)
            ),
            wordProblems = listOf(
                WordProblem(
                    "Tom has 8 books. He gives 3 books to his friend. How many books does Tom have left?",
                    5
                )
            )
        ),
        // Assessment 5
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(3, 6, 2, 4, 7, 8),
            numberRecognitionList = listOf(25, 74, 50, 32, 39, 58),
            additions = listOf(
                NumeracyOperations(28, 40, 68),
                NumeracyOperations(20, 38, 58),
                NumeracyOperations(87, 13, 100)
            ),
            subtractions = listOf(
                NumeracyOperations(34, 18, 16),
                NumeracyOperations(40, 16, 24),
                NumeracyOperations(90, 51, 39)
            ),
            multiplications = listOf(
                NumeracyOperations(8, 4, 32),
                NumeracyOperations(5, 5, 25),
                NumeracyOperations(6, 6, 36)
            ),
            divisions = listOf(
                NumeracyOperations(9, 3, 3),
                NumeracyOperations(18, 6, 3),
                NumeracyOperations(15, 5, 3)
            ),
            wordProblems = listOf(
                WordProblem(
                    "Sue has 9 apples. She eats 4 apples. How many apples does Sue have left?",
                    5
                )
            )
        ),

        //Dignitas
        NumeracyAssessmentContent(
            countAndMatchNumbersList = listOf(
                // N1, N2 (count/match objects → represented as sample numbers)
                3, 4, 5, 6, 7
            ),
            numberRecognitionList = listOf(
                // N6–N12
                0, 9, 8, 2, 30, 48, 97, 84, 22
            ),
            additions = listOf(
                NumeracyOperations(
                    firstNumber = 3,
                    secondNumber = 4,
                    answer = 7,
                    operationType = OperationType.ADDITION
                ),
                NumeracyOperations(
                    firstNumber = 5,
                    secondNumber = 4,
                    answer = 9,
                    operationType = OperationType.ADDITION
                )
            ),
            subtractions = listOf(
                NumeracyOperations(
                    firstNumber = 8,
                    secondNumber = 2,
                    answer = 6,
                    operationType = OperationType.SUBTRACTION
                ),
                NumeracyOperations(
                    firstNumber = 7,
                    secondNumber = 2,
                    answer = 5,
                    operationType = OperationType.SUBTRACTION
                )
            ),
            multiplications = emptyList(),
            divisions = emptyList(),
            wordProblems = listOf(
                WordProblem(
                    problem = "5 balls plus 4 balls = ?",
                    answer = 9
                ),
                WordProblem(
                    problem = "7 cups take away 2 cups = ?",
                    answer = 5
                )
            )
        )


    )
)

