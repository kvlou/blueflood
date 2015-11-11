package com.rackspacecloud.blueflood.tracker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Set;

public class TrackerTest {
    Tracker tracker;
    String testTenant1 = "tenant1";
    String testTenant2 = "tenant2";

    @Before
    public void setUp() {
        tracker = new Tracker();
    }

    @Test
    public void testAddTenant() {
        tracker.addTenant(testTenant1);

        Set tenants = tracker.getTenants();
        Assert.assertTrue("tenant " + testTenant1 + " not added", tracker.isTracking(testTenant1));
        Assert.assertTrue("tenants.size not 1", tenants.size() == 1);
        Assert.assertTrue("tenants does not contain " + testTenant1, tenants.contains(testTenant1));
    }

    @Test
    public void testDoesNotAddTenantTwice() {
        tracker.addTenant(testTenant1);
        tracker.addTenant(testTenant1);

        Set tenants = tracker.getTenants();
        Assert.assertTrue("tenant " + testTenant1 + " not added", tracker.isTracking(testTenant1));
        Assert.assertTrue("tenants.size not 1", tenants.size() == 1);
    }

    @Test
    public void testRemoveTenant() {
        tracker.addTenant(testTenant1);
        Assert.assertTrue("tenant " + testTenant1 + " not added", tracker.isTracking(testTenant1));

        tracker.removeTenant(testTenant1);

        Set tenants = tracker.getTenants();
        Assert.assertFalse("tenant " + testTenant1 + " not removed", tracker.isTracking(testTenant1));
        Assert.assertEquals("tenants.size not 0", tenants.size(), 0);
        Assert.assertFalse("tenants contains " + testTenant1, tenants.contains(testTenant1));
    }

    @Test
    public void testAddAndRemoveMetricName() {
        tracker.addMetricName("metricName");
        Assert.assertTrue("metricName not added",tracker.doesMessageContainMetricNames("Track.this.metricName"));

        tracker.addMetricName("anotherMetricNom");
        Assert.assertTrue("metricName not being logged",tracker.doesMessageContainMetricNames("Track.this.metricName"));
        Assert.assertTrue("anotherMetricNom not being logged",tracker.doesMessageContainMetricNames("Track.this.anotherMetricNom"));
        Assert.assertFalse("randomMetricNameNom should not be logged", tracker.doesMessageContainMetricNames("Track.this.randomMetricNameNom"));

        tracker.removeMetricName("metricName");
        Assert.assertFalse("metricName should not be logged",tracker.doesMessageContainMetricNames("Track.this.metricName"));

        tracker.removeMetricName("anotherMetricNom");
        Assert.assertTrue("Everything should be logged", tracker.doesMessageContainMetricNames("Track.this.anotherMetricNom"));
        Assert.assertTrue("Everything should be logged", tracker.doesMessageContainMetricNames("Track.this.randomMetricNameNom"));
    }

    @Test
    public void testRemoveAllMetricNames() {
        tracker.addMetricName("metricName");
        tracker.addMetricName("anotherMetricNom");

        Assert.assertTrue("metricName not being logged",tracker.doesMessageContainMetricNames("Track.this.metricName"));
        Assert.assertTrue("anotherMetricNom not being logged",tracker.doesMessageContainMetricNames("Track.this.anotherMetricNom"));
        Assert.assertFalse("randomMetricNameNom should not be logged", tracker.doesMessageContainMetricNames("Track.this.randomMetricNameNom"));

        tracker.removeAllMetricNames();

        Assert.assertTrue("Everything should be logged",tracker.doesMessageContainMetricNames("Track.this.metricName"));
        Assert.assertTrue("Everything should be logged", tracker.doesMessageContainMetricNames("Track.this.anotherMetricNom"));
        Assert.assertTrue("Everything should be logged", tracker.doesMessageContainMetricNames("Track.this.randomMetricNameNom"));
    }

    @Test
    public void testRemoveAllTenant() {
        tracker.addTenant(testTenant1);
        tracker.addTenant(testTenant2);
        Assert.assertTrue("tenant " + testTenant1 + " not added", tracker.isTracking(testTenant1));
        Assert.assertTrue("tenant " + testTenant2 + " not added", tracker.isTracking(testTenant2));

        tracker.removeAllTenants();
        Assert.assertFalse("tenant " + testTenant1 + " not removed", tracker.isTracking(testTenant1));
        Assert.assertFalse("tenant " + testTenant2 + " not removed", tracker.isTracking(testTenant2));

        Set tenants = tracker.getTenants();
        Assert.assertEquals("tenants.size not 0", tenants.size(), 0);
    }
}
