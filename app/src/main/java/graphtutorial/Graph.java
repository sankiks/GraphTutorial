package graphtutorial;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DeviceCodeCredential;
import com.azure.identity.DeviceCodeCredentialBuilder;
import com.azure.identity.DeviceCodeInfo;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.UserSendMailParameterSet;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;

import okhttp3.Request;

public class Graph {

	private static Properties _properties;
	private static DeviceCodeCredential _deviceCodeCredential;
	private static GraphServiceClient<Request> _userClient;

	public static void initializeGraphForUserAuth(Properties properties, Consumer<DeviceCodeInfo> challenge)
			throws Exception {
		// Ensure properties isn't null
		if (properties == null) {
			throw new Exception("Properties cannot be null");
		}

		_properties = properties;

		final String clientId = properties.getProperty("app.clientId");
		final String tenantId = properties.getProperty("app.tenantId");
		final List<String> graphUserScopes = Arrays.asList(properties.getProperty("app.graphUserScopes").split(","));

		_deviceCodeCredential = new DeviceCodeCredentialBuilder().clientId(clientId).tenantId(tenantId)
				.challengeConsumer(challenge).build();

		final TokenCredentialAuthProvider authProvider = new TokenCredentialAuthProvider(graphUserScopes,
				_deviceCodeCredential);

		_userClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
	}

}
