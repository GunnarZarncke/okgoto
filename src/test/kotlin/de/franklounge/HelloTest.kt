package de.franklounge

import de.franklounge.okgoto.service.clean
import kotlin.test.Test
import kotlin.test.assertEquals

class HelloTest {
    @Test
    fun test() {
        assertEquals(
            " Angela Merkel (2019) Unterschrift von Angela Merkel, 2011 '''Angela''' Die Aussprache des Namens ''Angela'' mit Betonung auf der ersten Silbe ist viel häufiger als mit Betonung auf der zweiten Silbe (außer in Österreich, siehe Duden online). Merkel bevorzugt jedoch die Betonung auf der zweiten Silbe, siehe Gerd Langguth: ''Angela Merkel.'' DTV, München 2005, ISBN 3-423-24485-2, S. 50. '''Dorothea Merkel''' (geb. ''Kasner''; * 17. Juli 1954 in Hamburg) ist eine deutsche Politikerin ( CDU). Sie war vom 22. November 2005 bis zum 8.&nbsp;Dezember 2021 achter Bundeskanzler und erste Bundeskanzlerin der Bundesrepublik Deutschland. Sie war in diesem Amt die erste Frau, die erste Person aus Ostdeutschland und die erste nach der Gründung der Bundesrepublik geborene Person. Von April 2000 bis Dezember 2018 war sie Bundesvorsitzende der CDU. ",
            clean(
                """
            {{Weiterleitungshinweis|Merkel}}
            [[Datei:Angela Merkel 2019 cropped.jpg|mini|Angela Merkel (2019)[[Datei:Accession Treaty 2011 Angela Merkel signature.svg|rahmenlos|zentriert|Unterschrift von Angela Merkel, 2011]]]]

            '''Angela'''<ref>Die Aussprache des Namens ''Angela'' mit Betonung auf der ersten Silbe ist viel häufiger als mit Betonung auf der zweiten Silbe (außer in Österreich, siehe [http://www.duden.de/rechtschreibung/Angela Duden online]). Merkel bevorzugt jedoch die Betonung auf der zweiten Silbe, siehe [[Gerd Langguth]]: ''Angela Merkel.'' DTV, München 2005, ISBN 3-423-24485-2, S. 50.</ref> '''Dorothea Merkel''' (geb. ''Kasner''; * [[17. Juli]] [[1954]] in [[Hamburg]]) ist eine [[Deutschland|deutsche]] [[Politiker]]in ([[Christlich Demokratische Union Deutschlands|CDU]]). Sie war vom 22. November 2005 bis zum 8.&nbsp;Dezember 2021 achter Bundeskanzler und erste [[Bundeskanzler (Deutschland)|Bundeskanzlerin der Bundesrepublik Deutschland]]. Sie war in diesem Amt die erste Frau, die erste Person aus [[Ostdeutschland]] und die erste nach der Gründung der Bundesrepublik geborene Person. Von April 2000 bis Dezember 2018 war sie [[Parteivorsitzender|Bundesvorsitzende]] der CDU.
        """
            )
        )
    }
}
