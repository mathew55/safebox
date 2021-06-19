package com.safebox.routes
import scala.concurrent.Future

case class User(id: Option[Long], name: String, age: Int, gender: Int) {
}
object UserDao {
//  def findAll(): Future[Seq[User]] = customersTable.result
//
//  def findById(id: Long): Future[User] = customersTable.filter(_.id === id).result.head
//
//  def create(user: User): Future[Long] = customersTable returning customersTable.map(_.id) += user
//
//  def update(id: Long, newCustomer: User): Future[Int] = {
//    customersTable.filter(_.id === id).map(customer => (customer.name, customer.age, customer.gender))
//      .update((newCustomer.name, newCustomer.age, newCustomer.gender))
//  }
//
//
//  def delete(id: Long): Future[Int] = {
//    customersTable.filter(_.id === id).delete
//  }

    def create(user: Int): String = "Return User"
}
