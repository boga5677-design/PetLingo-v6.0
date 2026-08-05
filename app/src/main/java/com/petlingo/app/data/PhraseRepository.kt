package com.petlingo.app.data

import com.petlingo.app.model.Phrase

class PhraseRepository {
    fun phrases(): List<Phrase> = listOf(
        Phrase(1, "take care of", "照顧；處理", "Please take care of this request today."),
        Phrase(2, "be responsible for", "負責", "She is responsible for customer service."),
        Phrase(3, "in charge of", "主管；負責", "Mr. Chen is in charge of the project."),
        Phrase(4, "as soon as possible", "儘快", "Please reply as soon as possible."),
        Phrase(5, "according to", "根據", "According to the schedule, the meeting starts at nine."),
        Phrase(6, "due to", "由於", "The flight was delayed due to bad weather."),
        Phrase(7, "look forward to", "期待", "We look forward to hearing from you."),
        Phrase(8, "on behalf of", "代表", "I am writing on behalf of our department."),
        Phrase(9, "in advance", "事先", "Please reserve your seat in advance."),
        Phrase(10, "in addition to", "除了……之外還", "In addition to English, she speaks Japanese."),
        Phrase(11, "fill out", "填寫", "Please fill out the registration form."),
        Phrase(12, "carry out", "執行", "The team will carry out a safety inspection."),
        Phrase(13, "set up", "設置；安排", "We need to set up the conference room."),
        Phrase(14, "follow up on", "追蹤處理", "I will follow up on your request tomorrow."),
        Phrase(15, "meet a deadline", "趕上期限", "The team worked late to meet the deadline."),
        Phrase(16, "make a reservation", "預約", "I would like to make a reservation for two."),
        Phrase(17, "be available", "有空；可取得", "The manager will be available after lunch."),
        Phrase(18, "at no additional charge", "不另收費", "Delivery is available at no additional charge."),
        Phrase(19, "with regard to", "關於", "I am contacting you with regard to your order."),
        Phrase(20, "take effect", "生效", "The new policy will take effect next month.")
    )
}
