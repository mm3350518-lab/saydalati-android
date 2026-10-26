import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

// ... داخل ملف SaydalatiPlusScreen وفي مكان الزر:

Button(
    onClick = {
        if (transactionId.isBlank()) {
            Toast.makeText(context, "يرجى إدخال رقم الإيصال أولاً", Toast.LENGTH_SHORT).show()
            return@Button
        }

        // جلب معرف المستخدم الحالي من Firebase Auth
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "يرجى تسجيل الدخول أولاً لتتمكن من إرسال الطلب", Toast.LENGTH_SHORT).show()
            return@Button
        }

        val db = FirebaseFirestore.getInstance()
        
        // تجهيز بيانات الطلب
        val subscriptionData = hashMapOf(
            "userId" to userId,
            "isSubscribed" to false, // غير مفعل لحين مراجعة الإدارة والإيصال
            "subscriptionType" to selectedPlan, // "monthly", "yearly", أو "lifetime"
            "paymentStatus" to "pending", // قيد المراجعة
            "transactionId" to transactionId,
            "requestDate" to Date()
        )

        // حفظ البيانات في جدول subscriptions
        db.collection("subscriptions").document(userId)
            .set(subscriptionData)
            .addOnSuccessListener {
                Toast.makeText(context, "تم إرسال طلبك بنجاح، بانتظار مراجعة الإدارة وتفعيل الحساب", Toast.LENGTH_LONG).show()
                onNavigateBack() // الرجوع للشاشة السابقة بعد الإرسال الناجح
            }
            .addOnFailureError { e ->
                Toast.makeText(context, "فشل الإرسال: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    },
    modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
    shape = RoundedCornerShape(12.dp)
) {
    Text("تأكيد وإرسال طلب الاشتراك", fontSize = 16.sp, fontWeight = FontWeight.Bold)
}