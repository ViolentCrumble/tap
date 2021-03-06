/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package models.graphql

import java.io.Serializable

import io.nlytx.expressions.data.{Coded, Reflect}
import sangria.macros.derive.{Interfaces, deriveObjectType}
import sangria.schema.{Argument, Context, Field, IntType, InterfaceType, ObjectType, OptionInputType, StringType, fields}
import io.heta.tap.data._

import scala.concurrent.Future

object Fields {

//    abstract class TapField {
//        val name:String
//        val description: Option[String]
//        val arguments: List[Argument[Option[String]]]
//        val deriveType: ObjectType[Unit,_<:Result]
//        def resolver(actions: Context[GraphqlActions,Unit]):Future[_<:Result]
//    }





    object CleanField {
        import Fields.FieldTypes._
        val name ="clean"
        val description = Some("Cleans text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,StringResult](Interfaces[Unit,StringResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.clean(actions.argOpt(TEXT), actions.argOpt(PARAMETERS))
    }

    case class StringResult(analytics: String, message:String = "", querytime:Int = -1) extends Result

    object AnnotationsField {
        import Fields.FieldTypes._
        val name ="annotations"
        val description = Some("Returns sentences for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SentencesResult](Interfaces[Unit,SentencesResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.annotations(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class SentencesResult(analytics: Vector[TapSentence], message:String = "", querytime:Int = -1) extends Result

    object VocabularyField {
        import Fields.FieldTypes._
        val name ="vocabulary"
        val description = Some("Returns vocabulary for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,VocabResult](Interfaces[Unit,VocabResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.vocabulary(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class VocabResult(analytics: TapVocab, message:String = "", querytime:Int = -1) extends Result

    object MetricsField {
        import Fields.FieldTypes._
        val name ="metrics"
        val description = Some("Returns metrics for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,MetricsResult](Interfaces[Unit,MetricsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.metrics(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class MetricsResult(analytics: TapMetrics, message:String = "", querytime:Int = -1) extends Result

    object PosStatsField {
        import Fields.FieldTypes._
        val name ="posStats"
        val description = Some("Returns posStats for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,PosStatsResult](Interfaces[Unit,PosStatsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.posStats(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class PosStatsResult(analytics: TapPosStats, message:String = "", querytime:Int = -1) extends Result

    object SyllablesField {
        import Fields.FieldTypes._
        val name ="syllables"
        val description = Some("Counts syllables in words and calculates averages for sentences")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SyllablesResult](Interfaces[Unit,SyllablesResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.syllables(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class SyllablesResult(analytics: Vector[TapSyllables], message:String = "", querytime:Int = -1) extends Result

    object SpellingField {
        import Fields.FieldTypes._
        val name ="spelling"
        val description = Some("Returns spelling errors and suggestions for each sentence")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,SpellingResult](Interfaces[Unit,SpellingResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.spelling(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class SpellingResult(analytics: Vector[TapSpelling], message:String = "", querytime:Int = -1) extends Result

    object ExpressionsField {
        import Fields.FieldTypes._
        val name ="expressions"
        val description = Some("Returns expressions for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,ExpressionsResult](Interfaces[Unit,ExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.expressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class ExpressionsResult(analytics: Vector[TapExpressions], message:String = "", querytime:Int = -1) extends Result

    object ReflectExpressionsField {
        import Fields.FieldTypes._
        val name ="reflectExpressions"
        val description = Some("Returns reflection expressions for text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,ReflectExpressionsResult](Interfaces[Unit,ReflectExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.reflectExpressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }

    case class ReflectExpressionsResult(analytics: TapReflectExpressions, message:String = "", querytime:Int = -1) extends Result

    object AffectExpressionsField {
        import Fields.FieldTypes._
        val name = "affectExpressions"
        val description = Some("Returns affect expressions for text given optional parameters")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,AffectExpressionsResult](Interfaces[Unit, AffectExpressionsResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.affectExpressions(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }
    case class AffectExpressionsResult(analytics: Vector[TapAffectExpressions], message:String = "", querytime:Int = -1) extends Result

    object RhetoricalMovesField {
        import Fields.FieldTypes._
        val name ="moves"
        val description = Some("Returns a list of moves for the input text")
        val arguments = inputText :: parameters :: Nil
        val deriveType = deriveObjectType[Unit,StringListResult](Interfaces[Unit,StringListResult](ResultType))
        def resolver(actions: Context[GraphqlActions,Unit]) = actions.ctx.moves(actions.argOpt(TEXT),actions.argOpt(PARAMETERS))
    }
    case class StringListResult(analytics: Vector[Vector[String]], message:String = "", querytime:Int = -1) extends Result

    object FieldTypes {

        val TEXT = "text"
        val PARAMETERS = "parameters"

        val inputText:Argument[Option[String]] = Argument(TEXT, OptionInputType(StringType))
        val parameters:Argument[Option[String]] = Argument(PARAMETERS,OptionInputType(StringType))

        implicit val ResultType:InterfaceType[Unit,Result] = InterfaceType(
            "Result", fields[Unit, Result](
                Field("timestamp", StringType, resolve = _.value.timestamp),
                Field("querytime", IntType, resolve = _.value.querytime),
                Field("message", StringType, resolve = _.value.message)
            )
        )

        implicit val TokenType:ObjectType[Unit,TapToken] = deriveObjectType[Unit,TapToken]()
        implicit val SentenceType:ObjectType[Unit,TapSentence] = deriveObjectType[Unit,TapSentence]()
        implicit val TermCountType:ObjectType[Unit,TermCount] = deriveObjectType[Unit,TermCount]()
        implicit val VocabType:ObjectType[Unit,TapVocab] = deriveObjectType[Unit,TapVocab]()
        implicit val MetricsType:ObjectType[Unit,TapMetrics] = deriveObjectType[Unit,TapMetrics]()
        implicit val TapExpressionType:ObjectType[Unit,TapExpression] = deriveObjectType[Unit,TapExpression]()
        implicit val TapExpressionsType:ObjectType[Unit,TapExpressions] = deriveObjectType[Unit,TapExpressions]()
        implicit val tapSyllablesType:ObjectType[Unit,TapSyllables] = deriveObjectType[Unit,TapSyllables]()
        implicit val TapSpellingType:ObjectType[Unit,TapSpelling] = deriveObjectType[Unit,TapSpelling]()
        implicit val TapSpellType:ObjectType[Unit,TapSpell] = deriveObjectType[Unit,TapSpell]()
        implicit val TapPosStatsType:ObjectType[Unit,TapPosStats] = deriveObjectType[Unit,TapPosStats]()
        implicit val TapMetaTagSummaryType:ObjectType[Unit,TapMetaTagSummary] = deriveObjectType[Unit,TapMetaTagSummary]()
        implicit val TapPhraseTagSummaryType:ObjectType[Unit,TapPhraseTagSummary] = deriveObjectType[Unit,TapPhraseTagSummary]()
        implicit val TapSummaryType:ObjectType[Unit,TapSummary] = deriveObjectType[Unit,TapSummary]()
        implicit val ReflectType:ObjectType[Unit,Reflect] = deriveObjectType[Unit,Reflect]()
        implicit val CodedType:ObjectType[Unit,Coded] = deriveObjectType[Unit,Coded]()
        implicit val TapReflectExpressionsType:ObjectType[Unit,TapReflectExpressions] = deriveObjectType[Unit,TapReflectExpressions]()
        implicit val TapAffectExpressionType:ObjectType[Unit,TapAffectExpression] = deriveObjectType[Unit,TapAffectExpression]()
        implicit val TapAffectExpressionsType:ObjectType[Unit,TapAffectExpressions] = deriveObjectType[Unit,TapAffectExpressions]()
    }

}

