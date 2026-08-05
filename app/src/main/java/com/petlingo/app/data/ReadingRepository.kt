package com.petlingo.app.data

import com.petlingo.app.model.ReadingPassage
import com.petlingo.app.model.ReadingQuestion

class ReadingRepository {
    fun passages(): List<ReadingPassage> = listOf(
        ReadingPassage(
            1, "Office Relocation Notice", "公告",
            "Beginning August 15, Brightwell Consulting will operate from the 12th floor of Harbor Tower. Our telephone numbers and email addresses will remain unchanged. Visitors should check in at the ground-floor reception desk and bring photo identification.",
            listOf(
                ReadingQuestion("When will the company move?", listOf("August 5", "August 12", "August 15", "August 25"), 2, "The notice says the new office begins operating on August 15."),
                ReadingQuestion("What must visitors bring?", listOf("A laptop", "Photo identification", "A printed invitation", "A parking permit"), 1, "Visitors must bring photo identification.")
            )
        ),
        ReadingPassage(
            2, "Training Registration Email", "電子郵件",
            "To: All sales staff. Registration for the customer-service workshop closes this Friday at 5 p.m. The workshop will be held next Tuesday from 9 a.m. to noon in Conference Room B. Please reply to this email only if you require a vegetarian lunch.",
            listOf(
                ReadingQuestion("Who should register?", listOf("All sales staff", "New customers", "Only managers", "Conference visitors"), 0, "The email is addressed to all sales staff."),
                ReadingQuestion("Why should an employee reply?", listOf("To change the date", "To reserve a seat", "To request a vegetarian lunch", "To cancel the workshop"), 2, "A reply is requested only for a vegetarian lunch.")
            )
        ),
        ReadingPassage(
            3, "Weekend Museum Promotion", "廣告",
            "This weekend only, City Science Museum members may bring one guest free of charge. The planetarium show requires a separate ticket, and online reservations are strongly recommended because seating is limited.",
            listOf(
                ReadingQuestion("What benefit do members receive?", listOf("Free parking", "One free guest", "A free planetarium ticket", "A gift-shop discount"), 1, "Members may bring one guest free of charge."),
                ReadingQuestion("Why are online reservations recommended?", listOf("The museum is closed", "Tickets are cheaper online", "Seating is limited", "Members need new cards"), 2, "The advertisement states that seating is limited.")
            )
        )
    )
}
