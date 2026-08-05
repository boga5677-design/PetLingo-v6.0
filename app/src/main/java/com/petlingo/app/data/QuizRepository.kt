package com.petlingo.app.data

import com.petlingo.app.model.QuestionType
import com.petlingo.app.model.QuizMode
import com.petlingo.app.model.QuizQuestion
import com.petlingo.app.model.Word

class QuizRepository {
    fun createQuiz(
        words: List<Word>,
        count: Int = 10,
        mode: QuizMode = QuizMode.ENGLISH_TO_CHINESE
    ): List<QuizQuestion> {
        if (mode == QuizMode.TOEIC_MOCK) return toeicMock(count)
        if (words.size < 4) return sampleQuestions()

        return words.shuffled()
            .take(count.coerceAtMost(words.size))
            .mapIndexed { index, word ->
                when (mode) {
                    QuizMode.CHINESE_TO_ENGLISH -> {
                        val distractors = words.asSequence()
                            .filter { it.id != word.id }
                            .shuffled()
                            .take(3)
                            .map { it.english }
                            .toList()
                        val options = (distractors + word.english).shuffled()
                        QuizQuestion(
                            id = index,
                            prompt = word.chinese,
                            options = options,
                            correctIndex = options.indexOf(word.english),
                            explanation = "${word.english}：${word.chinese}",
                            type = QuestionType.VOCABULARY
                        )
                    }

                    QuizMode.LISTENING -> {
                        val distractors = words.asSequence()
                            .filter { it.id != word.id }
                            .shuffled()
                            .take(3)
                            .map { it.chinese }
                            .toList()
                        val options = (distractors + word.chinese).shuffled()
                        QuizQuestion(
                            id = index,
                            prompt = word.english,
                            options = options,
                            correctIndex = options.indexOf(word.chinese),
                            explanation = "${word.english}：${word.chinese}",
                            type = QuestionType.LISTENING
                        )
                    }

                    else -> {
                        val distractors = words.asSequence()
                            .filter { it.id != word.id }
                            .shuffled()
                            .take(3)
                            .map { it.chinese }
                            .toList()
                        val options = (distractors + word.chinese).shuffled()
                        QuizQuestion(
                            id = index,
                            prompt = word.english,
                            options = options,
                            correctIndex = options.indexOf(word.chinese),
                            explanation = "${word.english}：${word.chinese}",
                            type = QuestionType.VOCABULARY
                        )
                    }
                }
            }
    }

    private fun toeicMock(count: Int): List<QuizQuestion> {
        val bank = listOf(
            QuizQuestion(1001, "All employees must ___ the safety training by Friday.", listOf("complete", "completed", "completing", "completion"), 0, "must 後接原形動詞 complete。", QuestionType.GRAMMAR),
            QuizQuestion(1002, "The new policy will take ___ on May 1.", listOf("effect", "affect", "effective", "effectively"), 0, "固定片語 take effect 表示生效。", QuestionType.GRAMMAR),
            QuizQuestion(1003, "Ms. Lee is responsible ___ preparing the monthly report.", listOf("for", "to", "at", "with"), 0, "be responsible for 是固定用法。", QuestionType.GRAMMAR),
            QuizQuestion(1004, "Please submit the form ___ noon tomorrow.", listOf("by", "during", "since", "among"), 0, "by 表示不晚於某時間。", QuestionType.GRAMMAR),
            QuizQuestion(1005, "The shipment was delayed ___ severe weather.", listOf("due to", "instead of", "except for", "along with"), 0, "due to 表示由於。", QuestionType.GRAMMAR),
            QuizQuestion(1006, "Customers who register online will receive a confirmation email. What will online customers receive?", listOf("A confirmation email", "A refund", "A free meal", "A printed catalog"), 0, "題幹明確指出會收到確認電子郵件。", QuestionType.READING),
            QuizQuestion(1007, "The office will be closed Monday for maintenance and reopen Tuesday at 8 a.m. When will it reopen?", listOf("Monday morning", "Monday evening", "Tuesday at 8 a.m.", "Wednesday"), 2, "公告指出星期二上午八點重新開放。", QuestionType.READING),
            QuizQuestion(1008, "Please reply only if you require a vegetarian lunch. Why should a person reply?", listOf("To request a vegetarian lunch", "To cancel the meeting", "To ask for parking", "To change a password"), 0, "reply only if 對應素食午餐需求。", QuestionType.READING),
            QuizQuestion(1009, "available", listOf("ready for use", "very expensive", "already broken", "hard to explain"), 0, "available 表示可取得、可使用或有空。", QuestionType.VOCABULARY),
            QuizQuestion(1010, "purchase", listOf("buy", "repair", "deliver", "cancel"), 0, "purchase 作動詞表示購買。", QuestionType.VOCABULARY),
            QuizQuestion(1011, "The manager asked us to follow up ___ the customer complaint.", listOf("on", "in", "of", "by"), 0, "follow up on 表示追蹤處理。", QuestionType.GRAMMAR),
            QuizQuestion(1012, "Visitors should check in at reception and bring photo identification. What should visitors bring?", listOf("Photo identification", "A laptop", "A parking ticket", "A uniform"), 0, "文章直接要求攜帶身分證明。", QuestionType.READING)
        )
        return List(count.coerceIn(10, 100)) { index ->
            bank[index % bank.size].copy(id = 2000 + index)
        }.shuffled()
    }

    private fun sampleQuestions() = listOf(
        QuizQuestion(1, "abandon", listOf("拋棄", "接受", "安排", "改善"), 0, "abandon：拋棄、放棄", QuestionType.VOCABULARY),
        QuizQuestion(2, "Please ___ the report by Friday.", listOf("submit", "submits", "submitted", "submitting"), 0, "祈使句使用原形動詞 submit。", QuestionType.GRAMMAR),
        QuizQuestion(3, "The meeting starts at 9:30. When should attendees arrive?", listOf("Before 9:30", "At noon", "After lunch", "Tomorrow"), 0, "依文章資訊應於會議開始前抵達。", QuestionType.READING)
    )
}
