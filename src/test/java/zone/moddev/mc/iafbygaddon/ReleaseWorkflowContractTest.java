package zone.moddev.mc.iafbygaddon;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/** Keeps the release contract separate from the furniture catalog checks. */
public class ReleaseWorkflowContractTest {
    private static String workflow() throws Exception {
        return new String(Files.readAllBytes(Paths.get(".github/workflows/publish-release.yml")),
                StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test public void publishingRequiresTheApprovedProjectAndBothParents() throws Exception {
        String text = workflow();
        assertTrue(text.contains("MinecraftModDevelopmentMods/IronAgeFurniture-OhTheBiomes-Addon"));
        assertTrue(text.contains("\"$curseforge_project_id\" != \"1707176\""));
        assertTrue(text.contains("iron-age-furniture,oh-the-biomes-youll-go"));
        assertTrue(text.contains("${project_slug}(required)"));
        assertFalse(text.contains("(optional)"));
    }

    @Test public void publicationUsesOneBundleAfterAllChecksAndCredentialConfirmation() throws Exception {
        String text = workflow();
        for (String check : new String[] {"Build, test, and audit", "Cold bootstrap and reproducibility",
                "Analyze Java", "validation"}) assertTrue(text.contains("'" + check + "'"));
        assertTrue(text.contains("confirm_live_publication:"));
        assertTrue(text.contains("Required release secret is unavailable"));
        assertTrue(text.contains("      - release_confirmation\n"));
        assertTrue(text.contains("      - create_release_tag\n"));
        assertTrue(text.contains("      - publish_maven\n"));
        assertTrue(text.contains("      - publish_curseforge\n"));
        assertTrue(text.contains("sha256sum --check SHA256SUMS"));
        assertTrue(text.contains("-PpreparedReleaseDir=\"$RUNNER_TEMP/release-input\""));
        assertTrue(text.contains("ref: ${{ needs.preflight.outputs.release_sha }}"));
        assertTrue(text.contains("Existing tag $RELEASE_TAG resolves to $tag_sha instead of $RELEASE_SHA"));
        assertTrue(text.contains("cancel-in-progress: false"));
        assertFalse(text.contains("--force"));
    }
}
