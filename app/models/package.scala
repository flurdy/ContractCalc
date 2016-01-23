
package models {

   object IntegerFormatter {
      def formatter = java.text.NumberFormat.getIntegerInstance
   }
}

package object models {

   implicit class BooleanFold(boolean: Boolean){
      def fold[B](l: B)(r:(B) => B): B = if(boolean) r(l) else l
   }

}
