import dispatch._
import net.liftweb.json._
import org.streum.configrity._
import org.scala_tools.time.Imports._

case class Time(
  Comment: String,
  Date: String,
  Hours: Int)

case class Project(
  ApproverEmployeeId: Int,
  CalculateOvertime: Boolean,
  Code: String,
  Name: String,
  TimeEntries: List[Time])

case class Employee(
  Id: Int,
  LockDate: String,
  Name: String,
  WeekNr: Int,
  Projects: List[Project])

class TimeKeeper() {
  val config = Configuration.load("credentials.conf")
  val username = config[String]("username")
  val password = config[String]("password")
  implicit val formats = DefaultFormats

  private def get(relativeUrl: String): JValue = {
    val http = new Http
    val req = url("https://intern.bekk.no/api/Timekeeper.svc/" + relativeUrl).as_!(username, password)
    val data = http(req as_str)
    parse(data)
  }

  def getEmployee(year: Int, month: Int, day: Int): Employee = {
    val json = get("Employee/20120522") // + year + month + day)
    json.extract[Employee]
  }

  //def getBillableHoursForMonth(monthNumber: Int) = List[Time] {}
}

object Main extends App {
  val t = new TimeKeeper()
  println("Employee: " + t.getEmployee(2012, 05, 22))
  println(DateTime.now.month.getAsString())
}