package org.art.demo.spock

import spock.lang.Specification
import spock.lang.Subject


class ExampleSpecification extends Specification {
    @Subject
    private Polygon polygon = new Polygon(5)


    def "Should be simple assertion"() {
        expect:
        1 == 1
    }

    def "Should demonstrate given-when-then"() {
        when:
        int sides = new Polygon(4).numberOfSides
        then:
        sides == 4
    }

    def "Should expect Exceptions"() {
        when:
        new Polygon(0)
        then:
        def exception = thrown(TooFewSidesException.class)
        exception.numberOfSides == 0
    }

    def "Should expect an Exception to be thrown a number of invalid input : #sides"() {
        when:
        new Polygon(sides)
        then:
        def exception = thrown(TooFewSidesException.class)
        exception.numberOfSides == sides
        where:
        sides << [-1, 0, 1, 2]
    }

    def "Should be able to create polygon with #sides sides"() {
        expect:
        new Polygon(sides).numberOfSides == sides
        where:
        sides << [3, 4, 5, 8, 14]
    }

    def "Should use data tables for calculating max. Max of #a and #b is #max"() {
        expect:
        Math.max(a, b) == max
        where:
        a | b || max
        1 | 3 || 3
        7 | 4 || 7
        0 | 0 || 0
    }

    def "Should be able to mock concrete class"() {
        given:
        Renderer renderer = Mock()
        @Subject
        def polygon = new Polygon(4, renderer)
        when:
        polygon.draw()
        then:
        4 * renderer.drawLine()
    }

    def "Should be able to create stub"() {
        given: "A palette with red as a primary colour"
        Palette palette = Stub()
        palette.getPrimaryColour() >> Colour.Red
        and: "and a renderer with the red palette"
        @Subject
        def renderer = new Renderer(palette)
        expect: "the renderer to use the palette's primary as foreground"
        renderer.getForegroundColour() == Colour.Red
    }

    def "Should use a helper method"() {
        given:
        Renderer renderer = Mock()
        def shapeFactory = new ShapeFactory(renderer)

        when:
        def polygon = shapeFactory.createDefaultPolygon()
        then:
        checkDefaultShape(polygon, renderer)
    }

    private static void checkDefaultShape(Polygon polygon, Renderer renderer) {
        assert polygon.numberOfSides == 4
        assert polygon.renderer == renderer
    }

    def "Should demonstrate use of 'verifyAll'"() {
        given:
        Renderer renderer = Mock()
        def shapeFactory = new ShapeFactory(renderer)

        when:
        def polygon = shapeFactory.createDefaultPolygon()
        then:
        verifyAll(polygon) {
            numberOfSides == 4
            renderer == renderer
        }
    }
}
