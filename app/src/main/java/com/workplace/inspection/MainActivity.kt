package com.workplace.inspection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WorkplaceInspectionApp()
                }
            }
        }
    }
}

private enum class AppTab(val title: String, val icon: ImageVector) {
    Dashboard("Dashboard", Icons.Filled.Home),
    Personnel("Personel", Icons.Filled.People),
    Operations("Operasyon", Icons.Filled.Build),
    Audit("Denetim", Icons.Filled.Assignment),
    Extras("Ekstralar", Icons.Filled.MoreHoriz)
}

private data class Employee(
    val name: String,
    val role: String,
    val monthlySalary: Int,
    val isManager: Boolean
)

private data class OvertimeRecord(
    val employeeName: String,
    val hours: Int,
    val reason: String
)

private data class LeaveRequest(
    val employeeName: String,
    val leaveType: String,
    val days: Int,
    val approved: Boolean
)

private data class PayrollRun(
    val employeeName: String,
    val baseSalary: Int,
    val overtimePayment: Int,
    val deduction: Int,
    val netAmount: Int
)

private data class FaultReport(
    val area: String,
    val issue: String,
    val priority: String,
    val resolved: Boolean
)

private data class MaintenancePlan(
    val equipment: String,
    val owner: String,
    val dueDate: String
)

private data class AssetAssignment(
    val assetCode: String,
    val assignee: String,
    val location: String
)

private data class CalibrationPlan(
    val device: String,
    val interval: String,
    val responsible: String
)

private data class AuditCheck(
    val area: String,
    val status: String,
    val note: String,
    val critical: Boolean
)

private data class IncidentReport(
    val title: String,
    val severity: String,
    val owner: String
)

private data class CorrectiveAction(
    val action: String,
    val owner: String,
    val dueDate: String,
    val closed: Boolean
)

private data class VisitorLog(
    val visitorName: String,
    val company: String,
    val host: String,
    val purpose: String
)

private data class TrainingRecord(
    val topic: String,
    val audience: String,
    val date: String,
    val mandatory: Boolean
)

private data class ShiftPlan(
    val team: String,
    val date: String,
    val shiftType: String
)

private data class DocumentExpiry(
    val documentName: String,
    val owner: String,
    val expiryDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkplaceInspectionApp() {
    val moneyFormatter = remember { DecimalFormat("#,###") }
    val tabItems = remember { AppTab.entries }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val employees = remember {
        mutableStateListOf(
            Employee("Ahmet Kaya", "Isletme Muduru", 62000, true),
            Employee("Elif Yildiz", "Kalite Uzmani", 43000, false)
        )
    }
    val overtimeRecords = remember { mutableStateListOf<OvertimeRecord>() }
    val leaveRequests = remember { mutableStateListOf<LeaveRequest>() }
    val payrollRuns = remember { mutableStateListOf<PayrollRun>() }

    val faultReports = remember {
        mutableStateListOf(
            FaultReport("Depo", "Forklift sensor arizasi", "Yuksek", false)
        )
    }
    val maintenancePlans = remember { mutableStateListOf<MaintenancePlan>() }
    val assetAssignments = remember { mutableStateListOf<AssetAssignment>() }
    val calibrationPlans = remember { mutableStateListOf<CalibrationPlan>() }

    val auditChecks = remember { mutableStateListOf<AuditCheck>() }
    val incidents = remember { mutableStateListOf<IncidentReport>() }
    val correctiveActions = remember { mutableStateListOf<CorrectiveAction>() }

    val visitorLogs = remember { mutableStateListOf<VisitorLog>() }
    val trainingRecords = remember { mutableStateListOf<TrainingRecord>() }
    val shiftPlans = remember { mutableStateListOf<ShiftPlan>() }
    val documentExpiries = remember { mutableStateListOf<DocumentExpiry>() }

    val notify: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Is Yeri Denetim Uygulamasi", fontWeight = FontWeight.Bold)
                        Text("Yonetici, mesai, izin, maas, ariza ve daha fazlasi")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                tabItems.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (tabItems[selectedTab]) {
                AppTab.Dashboard -> DashboardScreen(
                    employees = employees,
                    overtimeRecords = overtimeRecords,
                    leaveRequests = leaveRequests,
                    payrollRuns = payrollRuns,
                    faultReports = faultReports,
                    maintenancePlans = maintenancePlans,
                    auditChecks = auditChecks,
                    incidents = incidents,
                    correctiveActions = correctiveActions,
                    trainingRecords = trainingRecords,
                    moneyFormatter = moneyFormatter
                )

                AppTab.Personnel -> PersonnelScreen(
                    employees = employees,
                    overtimeRecords = overtimeRecords,
                    leaveRequests = leaveRequests,
                    payrollRuns = payrollRuns,
                    moneyFormatter = moneyFormatter,
                    notify = notify
                )

                AppTab.Operations -> OperationsScreen(
                    faultReports = faultReports,
                    maintenancePlans = maintenancePlans,
                    assetAssignments = assetAssignments,
                    calibrationPlans = calibrationPlans,
                    notify = notify
                )

                AppTab.Audit -> AuditScreen(
                    auditChecks = auditChecks,
                    incidents = incidents,
                    correctiveActions = correctiveActions,
                    notify = notify
                )

                AppTab.Extras -> ExtrasScreen(
                    visitorLogs = visitorLogs,
                    trainingRecords = trainingRecords,
                    shiftPlans = shiftPlans,
                    documentExpiries = documentExpiries,
                    notify = notify
                )
            }
        }
    }
}

@Composable
private fun DashboardScreen(
    employees: List<Employee>,
    overtimeRecords: List<OvertimeRecord>,
    leaveRequests: List<LeaveRequest>,
    payrollRuns: List<PayrollRun>,
    faultReports: List<FaultReport>,
    maintenancePlans: List<MaintenancePlan>,
    auditChecks: List<AuditCheck>,
    incidents: List<IncidentReport>,
    correctiveActions: List<CorrectiveAction>,
    trainingRecords: List<TrainingRecord>,
    moneyFormatter: DecimalFormat
) {
    val monthlyOvertimeHours = overtimeRecords.sumOf { it.hours }
    val managerCount = employees.count { it.isManager }
    val pendingLeaves = leaveRequests.count { !it.approved }
    val openFaults = faultReports.count { !it.resolved }
    val openActions = correctiveActions.count { !it.closed }
    val nonConformance = auditChecks.count { it.status.equals("Uygunsuz", ignoreCase = true) }
    val totalPayroll = payrollRuns.sumOf { it.netAmount }
    val criticalIncidents = incidents.count { it.severity.equals("Yuksek", ignoreCase = true) }

    ScreenContainer {
        Text(
            text = "Canli Operasyon Ozeti",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        MetricRow(
            firstTitle = "Toplam Calisan",
            firstValue = employees.size.toString(),
            secondTitle = "Yonetici",
            secondValue = managerCount.toString()
        )

        MetricRow(
            firstTitle = "Toplam Mesai Saat",
            firstValue = monthlyOvertimeHours.toString(),
            secondTitle = "Bekleyen Izin",
            secondValue = pendingLeaves.toString()
        )

        MetricRow(
            firstTitle = "Acik Ariza",
            firstValue = openFaults.toString(),
            secondTitle = "Acik Aksiyon",
            secondValue = openActions.toString()
        )

        MetricRow(
            firstTitle = "Uygunsuzluk",
            firstValue = nonConformance.toString(),
            secondTitle = "Kritik Olay",
            secondValue = criticalIncidents.toString()
        )

        MetricCard(
            title = "Kayitli Net Maas Toplami",
            value = "${moneyFormatter.format(totalPayroll)} TL",
            subtitle = "Personel > Maas Hesapla ekranindan olusturulur"
        )

        SectionCard(
            title = "Yoneticiye Onerilen Oncelikler",
            subtitle = "Risk ve denetim odakli aksiyon listesi"
        ) {
            val priorities = buildList {
                if (pendingLeaves > 0) add("$pendingLeaves adet izin talebi beklemede.")
                if (openFaults > 0) add("$openFaults adet acik ariza var, bakim planini hizlandirin.")
                if (openActions > 0) add("$openActions adet duzeltici faaliyet kapanmamis.")
                if (nonConformance > 0) add("$nonConformance adet uygunsuzluk takibi gerekli.")
                if (trainingRecords.isEmpty()) add("Egitim kaydi eklenmedi, zorunlu egitimleri planlayin.")
            }

            if (priorities.isEmpty()) {
                Text("Takip edilmesi gereken kritik bir konu gorunmuyor.")
            } else {
                priorities.forEach { line ->
                    Text("• $line")
                }
            }
        }

        SectionCard(
            title = "Ek Ozellikler (Akla Gelmeyenler)",
            subtitle = "Kalibrasyon, ziyaretci, dokuman suresi, vardiya takibi"
        ) {
            Text("Uygulamada su bolumler de bulunur:")
            Text("• Kalibrasyon ve olcum cihazi takibi")
            Text("• Vardiya planlama")
            Text("• Ziyaretci giris cikis takibi")
            Text("• Dokuman son gecerlilik tarihi alarm listesi")
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PersonnelScreen(
    employees: MutableList<Employee>,
    overtimeRecords: MutableList<OvertimeRecord>,
    leaveRequests: MutableList<LeaveRequest>,
    payrollRuns: MutableList<PayrollRun>,
    moneyFormatter: DecimalFormat,
    notify: (String) -> Unit
) {
    var employeeName by rememberSaveable { mutableStateOf("") }
    var employeeRole by rememberSaveable { mutableStateOf("") }
    var employeeSalary by rememberSaveable { mutableStateOf("") }
    var employeeIsManager by rememberSaveable { mutableStateOf(false) }

    var overtimeEmployee by rememberSaveable { mutableStateOf("") }
    var overtimeHours by rememberSaveable { mutableStateOf("") }
    var overtimeReason by rememberSaveable { mutableStateOf("") }

    var leaveEmployee by rememberSaveable { mutableStateOf("") }
    var leaveType by rememberSaveable { mutableStateOf("") }
    var leaveDays by rememberSaveable { mutableStateOf("") }

    var payrollEmployee by rememberSaveable { mutableStateOf("") }
    var payrollBase by rememberSaveable { mutableStateOf("") }
    var payrollOvertime by rememberSaveable { mutableStateOf("") }
    var payrollDeduction by rememberSaveable { mutableStateOf("") }

    ScreenContainer {
        SectionCard(title = "Calisan Ekle", subtitle = "Yonetici rolunu secerek orgut yapisini kurun") {
            AppTextField("Calisan adi", employeeName) { employeeName = it }
            AppTextField("Gorev / unvan", employeeRole) { employeeRole = it }
            AppTextField(
                label = "Aylik brut maas",
                value = employeeSalary,
                keyboardType = KeyboardType.Number
            ) { employeeSalary = it }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Yonetici mi?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = employeeIsManager, onCheckedChange = { employeeIsManager = it })
            }
            Button(
                onClick = {
                    val salary = employeeSalary.toIntOrNull()
                    if (employeeName.isBlank() || employeeRole.isBlank() || salary == null) {
                        notify("Calisan eklemek icin zorunlu alanlari doldurun.")
                    } else {
                        employees.add(
                            Employee(
                                name = employeeName.trim(),
                                role = employeeRole.trim(),
                                monthlySalary = salary,
                                isManager = employeeIsManager
                            )
                        )
                        employeeName = ""
                        employeeRole = ""
                        employeeSalary = ""
                        employeeIsManager = false
                        notify("Calisan kaydi eklendi.")
                    }
                }
            ) {
                Text("Calisani Ekle")
            }
        }

        SectionCard(title = "Mesai Takibi", subtitle = "Personel bazli mesai kaydi") {
            AppTextField("Calisan adi", overtimeEmployee) { overtimeEmployee = it }
            AppTextField(
                label = "Mesai saati",
                value = overtimeHours,
                keyboardType = KeyboardType.Number
            ) { overtimeHours = it }
            AppTextField("Mesai nedeni", overtimeReason) { overtimeReason = it }
            Button(
                onClick = {
                    val hours = overtimeHours.toIntOrNull()
                    if (overtimeEmployee.isBlank() || overtimeReason.isBlank() || hours == null) {
                        notify("Mesai kaydi icin tum alanlari girin.")
                    } else {
                        overtimeRecords.add(
                            OvertimeRecord(
                                employeeName = overtimeEmployee.trim(),
                                hours = hours,
                                reason = overtimeReason.trim()
                            )
                        )
                        overtimeEmployee = ""
                        overtimeHours = ""
                        overtimeReason = ""
                        notify("Mesai kaydi eklendi.")
                    }
                }
            ) {
                Text("Mesai Ekle")
            }
        }

        SectionCard(title = "Izin Talebi", subtitle = "Bekleyen talepleri tek tikla onaylayabilirsiniz") {
            AppTextField("Calisan adi", leaveEmployee) { leaveEmployee = it }
            AppTextField("Izin tipi", leaveType) { leaveType = it }
            AppTextField(
                label = "Gun sayisi",
                value = leaveDays,
                keyboardType = KeyboardType.Number
            ) { leaveDays = it }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val days = leaveDays.toIntOrNull()
                        if (leaveEmployee.isBlank() || leaveType.isBlank() || days == null) {
                            notify("Izin talebi icin bilgiler eksik.")
                        } else {
                            leaveRequests.add(
                                LeaveRequest(
                                    employeeName = leaveEmployee.trim(),
                                    leaveType = leaveType.trim(),
                                    days = days,
                                    approved = false
                                )
                            )
                            leaveEmployee = ""
                            leaveType = ""
                            leaveDays = ""
                            notify("Izin talebi alindi.")
                        }
                    }
                ) {
                    Text("Talep Ekle")
                }
                Button(
                    onClick = {
                        val pendingIndex = leaveRequests.indexOfFirst { !it.approved }
                        if (pendingIndex == -1) {
                            notify("Bekleyen izin talebi yok.")
                        } else {
                            val request = leaveRequests[pendingIndex]
                            leaveRequests[pendingIndex] = request.copy(approved = true)
                            notify("${request.employeeName} izin talebi onaylandi.")
                        }
                    }
                ) {
                    Text("Ilk Bekleyeni Onayla")
                }
            }
        }

        SectionCard(title = "Maas Hesaplama", subtitle = "Brut + mesai - kesinti") {
            AppTextField("Calisan adi", payrollEmployee) { payrollEmployee = it }
            AppTextField(
                label = "Brut maas",
                value = payrollBase,
                keyboardType = KeyboardType.Number
            ) { payrollBase = it }
            AppTextField(
                label = "Mesai ek ucreti",
                value = payrollOvertime,
                keyboardType = KeyboardType.Number
            ) { payrollOvertime = it }
            AppTextField(
                label = "Kesinti",
                value = payrollDeduction,
                keyboardType = KeyboardType.Number
            ) { payrollDeduction = it }

            Button(
                onClick = {
                    val base = payrollBase.toIntOrNull()
                    val overtime = payrollOvertime.toIntOrNull()
                    val deduction = payrollDeduction.toIntOrNull()
                    if (payrollEmployee.isBlank() || base == null || overtime == null || deduction == null) {
                        notify("Maas hesaplamasi icin tum alanlari doldurun.")
                    } else {
                        val net = base + overtime - deduction
                        payrollRuns.add(
                            PayrollRun(
                                employeeName = payrollEmployee.trim(),
                                baseSalary = base,
                                overtimePayment = overtime,
                                deduction = deduction,
                                netAmount = net
                            )
                        )
                        payrollEmployee = ""
                        payrollBase = ""
                        payrollOvertime = ""
                        payrollDeduction = ""
                        notify("Maas hesaplandi ve kaydedildi.")
                    }
                }
            ) {
                Text("Maas Hesapla")
            }
        }

        SectionCard(title = "Son Kayitlar") {
            InfoList(
                emptyText = "Calisan yok.",
                lines = employees.takeLast(4).reversed().map {
                    val managerTag = if (it.isManager) " | yonetici" else ""
                    "${it.name} | ${it.role} | ${moneyFormatter.format(it.monthlySalary)} TL$managerTag"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Mesai kaydi yok.",
                lines = overtimeRecords.takeLast(4).reversed().map {
                    "${it.employeeName}: ${it.hours} saat | ${it.reason}"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Izin kaydi yok.",
                lines = leaveRequests.takeLast(4).reversed().map {
                    val state = if (it.approved) "onayli" else "bekliyor"
                    "${it.employeeName}: ${it.leaveType} (${it.days} gun) - $state"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Maas kaydi yok.",
                lines = payrollRuns.takeLast(4).reversed().map {
                    "${it.employeeName}: net ${moneyFormatter.format(it.netAmount)} TL"
                }
            )
        }
    }
}

@Composable
private fun OperationsScreen(
    faultReports: MutableList<FaultReport>,
    maintenancePlans: MutableList<MaintenancePlan>,
    assetAssignments: MutableList<AssetAssignment>,
    calibrationPlans: MutableList<CalibrationPlan>,
    notify: (String) -> Unit
) {
    var faultArea by rememberSaveable { mutableStateOf("") }
    var faultIssue by rememberSaveable { mutableStateOf("") }
    var faultPriority by rememberSaveable { mutableStateOf("") }

    var maintenanceEquipment by rememberSaveable { mutableStateOf("") }
    var maintenanceOwner by rememberSaveable { mutableStateOf("") }
    var maintenanceDate by rememberSaveable { mutableStateOf("") }

    var assetCode by rememberSaveable { mutableStateOf("") }
    var assetOwner by rememberSaveable { mutableStateOf("") }
    var assetLocation by rememberSaveable { mutableStateOf("") }

    var calibrationDevice by rememberSaveable { mutableStateOf("") }
    var calibrationInterval by rememberSaveable { mutableStateOf("") }
    var calibrationResponsible by rememberSaveable { mutableStateOf("") }

    ScreenContainer {
        SectionCard(title = "Ariza Kaydi", subtitle = "Saha, depo, uretim veya ofis kaynakli arizalar") {
            AppTextField("Ariza alani", faultArea) { faultArea = it }
            AppTextField("Ariza detayi", faultIssue) { faultIssue = it }
            AppTextField("Oncelik (Dusuk/Orta/Yuksek)", faultPriority) { faultPriority = it }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (faultArea.isBlank() || faultIssue.isBlank() || faultPriority.isBlank()) {
                            notify("Ariza eklemek icin tum alanlari doldurun.")
                        } else {
                            faultReports.add(
                                FaultReport(
                                    area = faultArea.trim(),
                                    issue = faultIssue.trim(),
                                    priority = faultPriority.trim(),
                                    resolved = false
                                )
                            )
                            faultArea = ""
                            faultIssue = ""
                            faultPriority = ""
                            notify("Ariza kaydi eklendi.")
                        }
                    }
                ) {
                    Text("Ariza Ekle")
                }
                Button(
                    onClick = {
                        val openIndex = faultReports.indexOfFirst { !it.resolved }
                        if (openIndex == -1) {
                            notify("Acik ariza yok.")
                        } else {
                            val entry = faultReports[openIndex]
                            faultReports[openIndex] = entry.copy(resolved = true)
                            notify("${entry.area} arizasi cozuldu olarak isaretlendi.")
                        }
                    }
                ) {
                    Text("Ilk Acik Arizayi Kapat")
                }
            }
        }

        SectionCard(title = "Bakim Plani", subtitle = "Periyodik bakim ve sorumluluk atamasi") {
            AppTextField("Ekipman", maintenanceEquipment) { maintenanceEquipment = it }
            AppTextField("Sorumlu", maintenanceOwner) { maintenanceOwner = it }
            AppTextField("Bir sonraki tarih", maintenanceDate) { maintenanceDate = it }
            Button(
                onClick = {
                    if (maintenanceEquipment.isBlank() || maintenanceOwner.isBlank() || maintenanceDate.isBlank()) {
                        notify("Bakim plani icin alanlar bos birakilamaz.")
                    } else {
                        maintenancePlans.add(
                            MaintenancePlan(
                                equipment = maintenanceEquipment.trim(),
                                owner = maintenanceOwner.trim(),
                                dueDate = maintenanceDate.trim()
                            )
                        )
                        maintenanceEquipment = ""
                        maintenanceOwner = ""
                        maintenanceDate = ""
                        notify("Bakim plani eklendi.")
                    }
                }
            ) {
                Text("Bakim Plani Ekle")
            }
        }

        SectionCard(title = "Zimmet Takibi", subtitle = "Laptop, cihaz, KKD gibi varliklar") {
            AppTextField("Varlik kodu", assetCode) { assetCode = it }
            AppTextField("Teslim alan", assetOwner) { assetOwner = it }
            AppTextField("Lokasyon", assetLocation) { assetLocation = it }
            Button(
                onClick = {
                    if (assetCode.isBlank() || assetOwner.isBlank() || assetLocation.isBlank()) {
                        notify("Zimmet kaydi icin tum alanlar gerekli.")
                    } else {
                        assetAssignments.add(
                            AssetAssignment(
                                assetCode = assetCode.trim(),
                                assignee = assetOwner.trim(),
                                location = assetLocation.trim()
                            )
                        )
                        assetCode = ""
                        assetOwner = ""
                        assetLocation = ""
                        notify("Zimmet kaydi olusturuldu.")
                    }
                }
            ) {
                Text("Zimmet Ekle")
            }
        }

        SectionCard(title = "Kalibrasyon Takibi", subtitle = "Olcum cihazlari ve periyot yonetimi") {
            AppTextField("Cihaz adi", calibrationDevice) { calibrationDevice = it }
            AppTextField("Periyot (Aylik/Uc Aylik vb.)", calibrationInterval) { calibrationInterval = it }
            AppTextField("Sorumlu kisi", calibrationResponsible) { calibrationResponsible = it }
            Button(
                onClick = {
                    if (calibrationDevice.isBlank() || calibrationInterval.isBlank() || calibrationResponsible.isBlank()) {
                        notify("Kalibrasyon kaydi eksik.")
                    } else {
                        calibrationPlans.add(
                            CalibrationPlan(
                                device = calibrationDevice.trim(),
                                interval = calibrationInterval.trim(),
                                responsible = calibrationResponsible.trim()
                            )
                        )
                        calibrationDevice = ""
                        calibrationInterval = ""
                        calibrationResponsible = ""
                        notify("Kalibrasyon plani eklendi.")
                    }
                }
            ) {
                Text("Kalibrasyon Ekle")
            }
        }

        SectionCard(title = "Operasyon Kayit Ozeti") {
            InfoList(
                emptyText = "Ariza kaydi yok.",
                lines = faultReports.takeLast(4).reversed().map {
                    val state = if (it.resolved) "kapali" else "acik"
                    "${it.area} | ${it.issue} | ${it.priority} | $state"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Bakim plani yok.",
                lines = maintenancePlans.takeLast(4).reversed().map {
                    "${it.equipment} - ${it.owner} - ${it.dueDate}"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Zimmet kaydi yok.",
                lines = assetAssignments.takeLast(4).reversed().map {
                    "${it.assetCode} -> ${it.assignee} (${it.location})"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Kalibrasyon plani yok.",
                lines = calibrationPlans.takeLast(4).reversed().map {
                    "${it.device} | ${it.interval} | ${it.responsible}"
                }
            )
        }
    }
}

@Composable
private fun AuditScreen(
    auditChecks: MutableList<AuditCheck>,
    incidents: MutableList<IncidentReport>,
    correctiveActions: MutableList<CorrectiveAction>,
    notify: (String) -> Unit
) {
    var checkArea by rememberSaveable { mutableStateOf("") }
    var checkStatus by rememberSaveable { mutableStateOf("") }
    var checkNote by rememberSaveable { mutableStateOf("") }
    var checkCritical by rememberSaveable { mutableStateOf(false) }

    var incidentTitle by rememberSaveable { mutableStateOf("") }
    var incidentSeverity by rememberSaveable { mutableStateOf("") }
    var incidentOwner by rememberSaveable { mutableStateOf("") }

    var actionTitle by rememberSaveable { mutableStateOf("") }
    var actionOwner by rememberSaveable { mutableStateOf("") }
    var actionDueDate by rememberSaveable { mutableStateOf("") }

    ScreenContainer {
        SectionCard(title = "Denetim Kontrol Maddesi", subtitle = "Uygun/Uygunsuz sonucunu kaydedin") {
            AppTextField("Denetim alani", checkArea) { checkArea = it }
            AppTextField("Durum (Uygun/Uygunsuz)", checkStatus) { checkStatus = it }
            AppTextField("Not", checkNote) { checkNote = it }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Kritik bulgu mu?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = checkCritical, onCheckedChange = { checkCritical = it })
            }
            Button(
                onClick = {
                    if (checkArea.isBlank() || checkStatus.isBlank() || checkNote.isBlank()) {
                        notify("Denetim kaydi eksik.")
                    } else {
                        auditChecks.add(
                            AuditCheck(
                                area = checkArea.trim(),
                                status = checkStatus.trim(),
                                note = checkNote.trim(),
                                critical = checkCritical
                            )
                        )
                        checkArea = ""
                        checkStatus = ""
                        checkNote = ""
                        checkCritical = false
                        notify("Denetim maddesi kaydedildi.")
                    }
                }
            ) {
                Text("Madde Ekle")
            }
        }

        SectionCard(title = "Olay / Kaza Bildirimi", subtitle = "Is guvenligi ve operasyon olaylari") {
            AppTextField("Olay basligi", incidentTitle) { incidentTitle = it }
            AppTextField("Siddet (Dusuk/Orta/Yuksek)", incidentSeverity) { incidentSeverity = it }
            AppTextField("Aksiyon sahibi", incidentOwner) { incidentOwner = it }
            Button(
                onClick = {
                    if (incidentTitle.isBlank() || incidentSeverity.isBlank() || incidentOwner.isBlank()) {
                        notify("Olay bildirimi icin tum alanlari girin.")
                    } else {
                        incidents.add(
                            IncidentReport(
                                title = incidentTitle.trim(),
                                severity = incidentSeverity.trim(),
                                owner = incidentOwner.trim()
                            )
                        )
                        incidentTitle = ""
                        incidentSeverity = ""
                        incidentOwner = ""
                        notify("Olay kaydi olusturuldu.")
                    }
                }
            ) {
                Text("Olay Ekle")
            }
        }

        SectionCard(title = "Duzeltici Faaliyet", subtitle = "Denetim sonrasi kapanis takibi") {
            AppTextField("Faaliyet", actionTitle) { actionTitle = it }
            AppTextField("Sorumlu", actionOwner) { actionOwner = it }
            AppTextField("Termin tarihi", actionDueDate) { actionDueDate = it }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (actionTitle.isBlank() || actionOwner.isBlank() || actionDueDate.isBlank()) {
                            notify("Faaliyet bilgileri eksik.")
                        } else {
                            correctiveActions.add(
                                CorrectiveAction(
                                    action = actionTitle.trim(),
                                    owner = actionOwner.trim(),
                                    dueDate = actionDueDate.trim(),
                                    closed = false
                                )
                            )
                            actionTitle = ""
                            actionOwner = ""
                            actionDueDate = ""
                            notify("Duzeltici faaliyet eklendi.")
                        }
                    }
                ) {
                    Text("Faaliyet Ekle")
                }
                Button(
                    onClick = {
                        val openIndex = correctiveActions.indexOfFirst { !it.closed }
                        if (openIndex == -1) {
                            notify("Acik faaliyet yok.")
                        } else {
                            val action = correctiveActions[openIndex]
                            correctiveActions[openIndex] = action.copy(closed = true)
                            notify("Faaliyet kapatildi: ${action.action}")
                        }
                    }
                ) {
                    Text("Ilk Acik Faaliyeti Kapat")
                }
            }
        }

        SectionCard(title = "Denetim Kayit Ozeti") {
            InfoList(
                emptyText = "Kontrol maddesi yok.",
                lines = auditChecks.takeLast(5).reversed().map {
                    val critical = if (it.critical) "kritik" else "normal"
                    "${it.area} - ${it.status} - $critical"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Olay kaydi yok.",
                lines = incidents.takeLast(5).reversed().map {
                    "${it.title} | ${it.severity} | ${it.owner}"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Faaliyet yok.",
                lines = correctiveActions.takeLast(5).reversed().map {
                    val state = if (it.closed) "kapali" else "acik"
                    "${it.action} - ${it.owner} - $state"
                }
            )
        }
    }
}

@Composable
private fun ExtrasScreen(
    visitorLogs: MutableList<VisitorLog>,
    trainingRecords: MutableList<TrainingRecord>,
    shiftPlans: MutableList<ShiftPlan>,
    documentExpiries: MutableList<DocumentExpiry>,
    notify: (String) -> Unit
) {
    var visitorName by rememberSaveable { mutableStateOf("") }
    var visitorCompany by rememberSaveable { mutableStateOf("") }
    var visitorHost by rememberSaveable { mutableStateOf("") }
    var visitorPurpose by rememberSaveable { mutableStateOf("") }

    var trainingTopic by rememberSaveable { mutableStateOf("") }
    var trainingAudience by rememberSaveable { mutableStateOf("") }
    var trainingDate by rememberSaveable { mutableStateOf("") }
    var mandatoryTraining by rememberSaveable { mutableStateOf(false) }

    var shiftTeam by rememberSaveable { mutableStateOf("") }
    var shiftDate by rememberSaveable { mutableStateOf("") }
    var shiftType by rememberSaveable { mutableStateOf("") }

    var documentName by rememberSaveable { mutableStateOf("") }
    var documentOwner by rememberSaveable { mutableStateOf("") }
    var documentExpiry by rememberSaveable { mutableStateOf("") }

    ScreenContainer {
        SectionCard(title = "Ziyaretci Takibi", subtitle = "Tedarikci, denetci, misafir logu") {
            AppTextField("Ziyaretci adi", visitorName) { visitorName = it }
            AppTextField("Sirket", visitorCompany) { visitorCompany = it }
            AppTextField("Ev sahibi", visitorHost) { visitorHost = it }
            AppTextField("Ziyaret amaci", visitorPurpose) { visitorPurpose = it }
            Button(
                onClick = {
                    if (visitorName.isBlank() || visitorCompany.isBlank() || visitorHost.isBlank() || visitorPurpose.isBlank()) {
                        notify("Ziyaretci kaydi icin tum alanlari doldurun.")
                    } else {
                        visitorLogs.add(
                            VisitorLog(
                                visitorName = visitorName.trim(),
                                company = visitorCompany.trim(),
                                host = visitorHost.trim(),
                                purpose = visitorPurpose.trim()
                            )
                        )
                        visitorName = ""
                        visitorCompany = ""
                        visitorHost = ""
                        visitorPurpose = ""
                        notify("Ziyaretci kaydi olusturuldu.")
                    }
                }
            ) {
                Text("Ziyaretci Ekle")
            }
        }

        SectionCard(title = "Egitim Takibi", subtitle = "Yasal ve operasyonel egitim yonetimi") {
            AppTextField("Egitim konusu", trainingTopic) { trainingTopic = it }
            AppTextField("Hedef grup", trainingAudience) { trainingAudience = it }
            AppTextField("Tarih", trainingDate) { trainingDate = it }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Zorunlu egitim mi?")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = mandatoryTraining, onCheckedChange = { mandatoryTraining = it })
            }
            Button(
                onClick = {
                    if (trainingTopic.isBlank() || trainingAudience.isBlank() || trainingDate.isBlank()) {
                        notify("Egitim kaydi eksik.")
                    } else {
                        trainingRecords.add(
                            TrainingRecord(
                                topic = trainingTopic.trim(),
                                audience = trainingAudience.trim(),
                                date = trainingDate.trim(),
                                mandatory = mandatoryTraining
                            )
                        )
                        trainingTopic = ""
                        trainingAudience = ""
                        trainingDate = ""
                        mandatoryTraining = false
                        notify("Egitim kaydi eklendi.")
                    }
                }
            ) {
                Text("Egitim Ekle")
            }
        }

        SectionCard(title = "Vardiya Planlama", subtitle = "Takim ve gun bazli planlama") {
            AppTextField("Takim", shiftTeam) { shiftTeam = it }
            AppTextField("Tarih", shiftDate) { shiftDate = it }
            AppTextField("Vardiya tipi", shiftType) { shiftType = it }
            Button(
                onClick = {
                    if (shiftTeam.isBlank() || shiftDate.isBlank() || shiftType.isBlank()) {
                        notify("Vardiya bilgileri eksik.")
                    } else {
                        shiftPlans.add(
                            ShiftPlan(
                                team = shiftTeam.trim(),
                                date = shiftDate.trim(),
                                shiftType = shiftType.trim()
                            )
                        )
                        shiftTeam = ""
                        shiftDate = ""
                        shiftType = ""
                        notify("Vardiya plani eklendi.")
                    }
                }
            ) {
                Text("Vardiya Ekle")
            }
        }

        SectionCard(title = "Dokuman Gecerlilik Takibi", subtitle = "Talimat, ruhsat ve sertifika son tarihleri") {
            AppTextField("Dokuman adi", documentName) { documentName = it }
            AppTextField("Sahibi", documentOwner) { documentOwner = it }
            AppTextField("Son gecerlilik tarihi", documentExpiry) { documentExpiry = it }
            Button(
                onClick = {
                    if (documentName.isBlank() || documentOwner.isBlank() || documentExpiry.isBlank()) {
                        notify("Dokuman kaydi eksik.")
                    } else {
                        documentExpiries.add(
                            DocumentExpiry(
                                documentName = documentName.trim(),
                                owner = documentOwner.trim(),
                                expiryDate = documentExpiry.trim()
                            )
                        )
                        documentName = ""
                        documentOwner = ""
                        documentExpiry = ""
                        notify("Dokuman son tarih kaydi eklendi.")
                    }
                }
            ) {
                Text("Dokuman Ekle")
            }
        }

        SectionCard(title = "Ekstra Moduller Ozeti") {
            InfoList(
                emptyText = "Ziyaretci kaydi yok.",
                lines = visitorLogs.takeLast(5).reversed().map {
                    "${it.visitorName} (${it.company}) -> ${it.host} | ${it.purpose}"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Egitim kaydi yok.",
                lines = trainingRecords.takeLast(5).reversed().map {
                    val mandatory = if (it.mandatory) "zorunlu" else "opsiyonel"
                    "${it.topic} | ${it.audience} | ${it.date} | $mandatory"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Vardiya plani yok.",
                lines = shiftPlans.takeLast(5).reversed().map {
                    "${it.team} - ${it.date} - ${it.shiftType}"
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoList(
                emptyText = "Dokuman takip kaydi yok.",
                lines = documentExpiries.takeLast(5).reversed().map {
                    "${it.documentName} | ${it.owner} | ${it.expiryDate}"
                }
            )
        }
    }
}

@Composable
private fun MetricRow(
    firstTitle: String,
    firstValue: String,
    secondTitle: String,
    secondValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            title = firstTitle,
            value = firstValue
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            title = secondTitle,
            value = secondValue
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun ScreenContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun SectionCard(
    title: String,
    subtitle: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            if (!subtitle.isNullOrBlank()) {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            content()
        }
    }
}

@Composable
private fun AppTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
private fun InfoList(emptyText: String, lines: List<String>) {
    if (lines.isEmpty()) {
        Text(emptyText, style = MaterialTheme.typography.bodyMedium)
        return
    }

    lines.forEachIndexed { index, line ->
        Text("${index + 1}. $line", style = MaterialTheme.typography.bodyMedium)
        if (index != lines.lastIndex) {
            HorizontalDivider(modifier = Modifier.padding(top = 6.dp))
        }
    }
}
