/*
 * Copyright (c) 2016-2018 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 */

package controllers.handlers

import io.heta.tap.data.AffectThresholds
import io.heta.tap.pipelines.AnnotatingTypes._
import io.heta.tap.pipelines.materialize.TextPipeline
import io.heta.tap.pipelines.{Annotating, Cleaning}
import javax.inject.Inject
import models.graphql.Fields._
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 20/2/17.
  */
class TextAnalysisHandler @Inject() (clean: Cleaning, annotate: Annotating) extends GenericHandler {

  private val pipe = annotate.Pipeline

  def clean(text: Option[String], parameters: Option[String], start:Long): Future[StringResult] = {
    val inputStr = text.getOrElse("")
    val cleanType = extractParameter[String]("cleanType",parameters)
    Logger.debug(s"STRING: $cleanType")
    val outputStr:Future[String] = cleanType match {
      case Some("visible") => TextPipeline(inputStr,clean.Pipeline.revealInvisible).run
      case Some("minimal") => TextPipeline(inputStr,clean.Pipeline.utfMinimal).run
      case Some("simple") => TextPipeline(inputStr,clean.Pipeline.utfSimplify).run
      case Some("preserve") => TextPipeline(inputStr,clean.Pipeline.lengthPreserve).run
      case Some("ascii") =>  TextPipeline(inputStr,clean.Pipeline.asciiOnly).run
      case _ => Future("")
    }
    outputStr.map(StringResult(_,querytime = queryTime(start)))
  }

  def annotations(text:Option[String],parameters:Option[String],start:Long):Future[SentencesResult] = {
    val inputStr = text.getOrElse("")
    val pipeType = extractParameter[String]("pipeType",parameters)
    Logger.debug(s"STRING: $pipeType")
    val analysis = pipeType match {
      case Some(CLU) => TextPipeline(inputStr, annotate.build(CLU,pipe.cluSentences)).run
      case Some(STANDARD) => TextPipeline(inputStr, annotate.build(STANDARD,pipe.sentences)).run
      case Some(FAST) => TextPipeline(inputStr, annotate.build(FAST,pipe.sentences)).run
      case Some(NER) => TextPipeline(inputStr, annotate.build(NER,pipe.sentences)).run
      case _ => TextPipeline(inputStr, annotate.build(FAST,pipe.sentences)).run
    }
    analysis.map(SentencesResult(_,querytime = queryTime(start)))
  }


  def expressions(text:Option[String],parameters:Option[String],start:Long):Future[ExpressionsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.expressions)).run
      .map(ExpressionsResult(_,querytime = queryTime(start)))

  def syllables(text:Option[String],parameters:Option[String],start:Long):Future[SyllablesResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.syllables)).run
      .map(SyllablesResult(_,querytime = queryTime(start)))

  def spelling(text:Option[String],parameters:Option[String],start:Long):Future[SpellingResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.spelling)).run
      .map(SpellingResult(_,querytime = queryTime(start)))

  def vocabulary(text:Option[String],parameters:Option[String],start:Long):Future[VocabResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.vocab)).run
      .map(VocabResult(_,querytime = queryTime(start)))

  def metrics(text:Option[String],parameters:Option[String],start:Long):Future[MetricsResult]  =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.metrics)).run
      .map(MetricsResult(_,querytime = queryTime(start)))

  def posStats(text:Option[String],parameters:Option[String],start:Long):Future[PosStatsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.posStats)).run
      .map(PosStatsResult(_,querytime = queryTime(start)))

  def reflectExpressions(text:Option[String],parameters:Option[String],start:Long):Future[ReflectExpressionsResult] =
    TextPipeline(text.getOrElse(""),annotate.build(DEFAULT,pipe.reflectExpress)).run
    .map(ReflectExpressionsResult(_, querytime = queryTime(start)))

  def affectExpressions(text:Option[String],parameters:Option[String] = None,start:Long): Future[AffectExpressionsResult] = {
    val thresholds = extractAffectThresholds(parameters)
    Logger.info(s"thresholds: $thresholds")
    TextPipeline(text.getOrElse(""),annotate.build(CLU,pipe.affectExpress(thresholds))).run
      .map(AffectExpressionsResult(_,"",queryTime(start)))
  }


  private def extractAffectThresholds(parameters:Option[String]):Option[AffectThresholds] = {
    for {
      v <- extractParameter[Double]("valence",parameters)
      a <- extractParameter[Double]("arousal",parameters)
      d <- extractParameter[Double]("dominance",parameters)
    } yield AffectThresholds(v.asInstanceOf[Double],a.asInstanceOf[Double],d.asInstanceOf[Double])
  }


  //TODO To be implemented
  def shape(text:String):Future[StringResult]   = dummyResult(text)


}
