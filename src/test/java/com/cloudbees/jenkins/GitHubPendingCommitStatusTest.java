package com.cloudbees.jenkins;

import hudson.model.Build;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.plugins.git.GitSCM;
import org.junit.Test;
import org.jvnet.hudson.test.Bug;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Tests for {@link GitHubPendingCommitStatus}.
 * @author Oleg Nenashev <o.v.nenashev@gmail.com>
 */
public class GitHubPendingCommitStatusTest extends HudsonTestCase {
    
    @Test
    public void testNoBuildData() throws Exception, InterruptedException  {
        FreeStyleProject prj = createFreeStyleProject("23641_noBuildData");
        prj.getBuildersList().add(new GitHubPendingCommitStatus());
        Build b = prj.scheduleBuild2(0).get();
        assertBuildStatus(Result.FAILURE, b);
        assertLogContains(org.jenkinsci.plugins.github.util.Messages.BuildDataHelper_NoBuildDataError(), b);
    }
    
    @Test
    public void testNoBuildRevision() throws Exception, InterruptedException {
        FreeStyleProject prj = createFreeStyleProject();
        prj.setScm(new GitSCM("http://non.existent.git.repo.nowhere/repo.git"));
        prj.getBuildersList().add(new GitHubPendingCommitStatus());
        Build b = prj.scheduleBuild2(0).get();
        assertBuildStatus(Result.FAILURE, b);
        assertLogContains(org.jenkinsci.plugins.github.util.Messages.BuildDataHelper_NoLastRevisionError(), b);
    }
}
